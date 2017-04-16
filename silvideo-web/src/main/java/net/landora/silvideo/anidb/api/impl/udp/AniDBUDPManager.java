/**
 * Copyright (C) 2012-2014 Blake Dickie
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.landora.silvideo.anidb.api.impl.udp;

import net.landora.silvideo.anidb.api.AniDBConnectionError;
import net.landora.silvideo.settings.SettingsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.annotation.PostConstruct;

/**
 *
 * @author bdickie
 */
@Component
public class AniDBUDPManager implements Lifecycle {

    private static final String UDP_ENCODING = "UTF8";
    private static final int UDP_MAX_PACKET_LENGTH = 1400;
    private static final int UDP_API_PROTO_VERSION = 3;

    @Value( "${anidb.udp.client-name}" )
    private String udpClientName;
    @Value( "${anidb.udp.client-version}" )
    private int udpClientVersion;

    @Value( "${anidb.udp.host}" )
    private String serverAddressString;
    @Value( "${anidb.udp.port}" )
    private int serverPort;

    @Value( "${anidb.udp.timeout}" )
    private int timeout;
    @Value( "${anidb.udp.auto-logout-delay}" )
    private long autoLogoutDelay;
    @Value( "${anidb.udp.delay}" )
    private long messageDelay;
    @Value( "${anidb.udp.quick-initial-calls}" )
    private int quickInitialCalls;
    @Value( "${anidb.udp.listen-port}" )
    private int listenPort;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private SettingsManager settingsManager;

    private int initialCalls = 0;

    private Logger log = LoggerFactory.getLogger( AniDBUDPManager.class );

    private boolean isShutdown;
    private InetAddress serverAddress;

    @PostConstruct
    public void postConstruct() throws UnknownHostException, SocketException {
        serverAddress = InetAddress.getByName( serverAddressString );

        socket = new DatagramSocket( listenPort );
        socket.setSoTimeout( timeout );
    }

    private DatagramSocket socket;

    private long lastMessageSent;

    private String sessionId;

    public synchronized String getSessionId() {
        return getSessionId( true );
    }

    public synchronized String getSessionId( boolean autoLogin ) {
        if ( sessionId == null && autoLogin ) {
            AniDBCommand loginCommand = new AniDBCommand( "AUTH", false );
            loginCommand.addArgument( "user", settingsManager.getSettings().getAnidbUsername() );
            loginCommand.addArgument( "pass", settingsManager.getSettings().getAnidbPassword() );
            loginCommand.addArgument( "protover", UDP_API_PROTO_VERSION );
            loginCommand.addArgument( "client", udpClientName );
            loginCommand.addArgument( "clientver", udpClientVersion );
//            loginCommand.addArgument("comp", "1");
            loginCommand.addArgument( "enc", UDP_ENCODING );

            AniDBReply reply = sendData( loginCommand );
            if ( reply == null ) {
                throw new AniDBConnectionError( "Unable to login to AniDB." );
            }

            switch ( reply.getReturnCode() ) {
                case 200:
                case 201:
                    String message = reply.getReturnMessage();
                    sessionId = message.split( "\\s", 2 )[0];
                    break;
                default:
                    throw new AniDBConnectionError( "Recieved login error: " + reply.getReturnCode() + " " + reply.getReturnMessage() );
            }

        }

        return sessionId;
    }

    private synchronized void logout() {
        if ( sessionId == null ) {
            return;
        }

        AniDBCommand logoutCommand = new AniDBCommand( "LOGOUT" );
        sendData( logoutCommand );
        sessionId = null;
    }

    public synchronized AniDBReply sendData( AniDBCommand call ) {
        if ( isShutdown ) {
            throw new AniDBConnectionError( "Shutdown is progress." );
        }

        for ( int i = 0; i < 2; i++ ) {
            if ( i == 1 && log.isTraceEnabled() ) {
                log.trace( "Retrying failed AniDB call." );
            }

            String sessionId;
            if ( call.isUsesSessions() ) {
                sessionId = getSessionId();
            } else {
                sessionId = null;
            }

            String replyStr = sendData( call.getResult( sessionId ) );

            if ( replyStr == null ) {
                continue;
            }

            AniDBReply reply = new AniDBReply( replyStr );

            switch ( reply.getReturnCode() ) {
                case 501:
                case 506:
                    // Need to reauthenticate.

                    sessionId = null;
                    break;

                default:
                    return reply;
            }

        }

        return null;
    }

    @SuppressWarnings( { "CallToNativeMethodWhileLocked", "SleepWhileHoldingLock" } )
    private synchronized String sendData( String message ) {
        try {
            if ( initialCalls < quickInitialCalls ) {
                initialCalls++;
            } else {
                long nextMessageTime = lastMessageSent + messageDelay;

                while ( System.currentTimeMillis() < nextMessageTime ) {
                    Thread.sleep( Math.max( 1, nextMessageTime - System.currentTimeMillis() ) );
                }
            }

            if ( log.isTraceEnabled() ) {
                log.trace( "Sending AniDB Command:\n" + message );
            }

            byte[] data = message.getBytes( UDP_ENCODING );

            DatagramPacket packet = new DatagramPacket( data, data.length, serverAddress, serverPort );

            socket.send( packet );

            lastMessageSent = System.currentTimeMillis();

            packet = new DatagramPacket( new byte[UDP_MAX_PACKET_LENGTH], UDP_MAX_PACKET_LENGTH );

            socket.receive( packet );

            String result = new String( packet.getData(), packet.getOffset(), packet.getLength(), UDP_ENCODING );

            if ( log.isTraceEnabled() ) {
                log.trace( "Recieved AniDB Reply:\n" + result );
            }

            return result;
        } catch ( Exception e ) {
            log.error( "Error sending message: " + message, e );
            return null;
        }
    }

    public synchronized void shutdown() {
        logout();

        socket.close();

        isShutdown = true;
    }

    @Scheduled( fixedRate = 60000 )
    public synchronized void logoutCheck() {
        if ( sessionId != null && System.currentTimeMillis() > lastMessageSent + autoLogoutDelay ) {
            logout();
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        shutdown();
    }

    @Override
    public boolean isRunning() {
        return !isShutdown;
    }

}
