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

import org.apache.commons.lang.StringEscapeUtils;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author bdickie
 */
public class AniDBCommand {

    private String command;
    private LinkedHashMap<String, String> arguments;
    private boolean usesSessions;

    public AniDBCommand( String command ) {
        this( command, true );
    }

    public AniDBCommand( String command, boolean usesSessions ) {
        this.command = command;
        this.usesSessions = usesSessions;

        arguments = new LinkedHashMap<String, String>();
    }

    private static void appendEscape( StringBuffer buffer, String str ) {
        str = StringEscapeUtils.escapeHtml( str );
        str = str.replaceAll( "\\n", "<br />" );
        buffer.append( str );
    }

    public boolean isUsesSessions() {
        return usesSessions;
    }

    public String getResult( String sessionId ) {
        StringBuffer buffer = new StringBuffer();

        buffer.append( command );

        boolean hasArg = false;
        if ( usesSessions || !arguments.isEmpty() ) {
            buffer.append( " " );

            for ( Map.Entry<String, String> entry : arguments.entrySet() ) {
                if ( hasArg ) {
                    buffer.append( "&" );
                } else {
                    hasArg = true;
                }

                addParameter( buffer, entry.getKey(), entry.getValue() );
            }

            if ( usesSessions ) {
                if ( hasArg ) {
                    buffer.append( "&" );
                } else {
                    hasArg = true;
                }

                addParameter( buffer, "s", sessionId );
            }
        }

        return buffer.toString();
    }

    private static void addParameter( StringBuffer buffer, String name, String value ) {
        appendEscape( buffer, name );
        buffer.append( "=" );
        appendEscape( buffer, value );
    }

    @Override
    public String toString() {
        return getResult( "XXXXX" );
    }

    public void addArgument( String name, String value ) {
        arguments.put( name, value );
    }

    public void addArgument( String name, int value ) {
        addArgument( name, String.valueOf( value ) );
    }

    public void addArgument( String name, long value ) {
        addArgument( name, String.valueOf( value ) );
    }

    public void addArgument( String name, Calendar value ) {
        if ( value == null ) {
            addArgument( name, 0 );
        } else {
            addArgument( name, value.getTimeInMillis() / 1000l );
        }
    }

}
