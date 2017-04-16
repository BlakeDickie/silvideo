/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.api.impl;

import net.landora.silvideo.anidb.AniDBInfo;
import net.landora.silvideo.anidb.api.AniDBAPI;
import net.landora.silvideo.anidb.api.AniDBConnectionError;
import net.landora.silvideo.anidb.api.WaitingNotification;
import net.landora.silvideo.anidb.api.impl.http.AnimeCategoryParser;
import net.landora.silvideo.anidb.api.impl.http.AnimeTitleParser;
import net.landora.silvideo.anidb.api.impl.udp.AniDBCommand;
import net.landora.silvideo.anidb.api.impl.udp.AniDBReply;
import net.landora.silvideo.anidb.api.impl.udp.AniDBUDPManager;
import net.landora.silvideo.anidb.api.impl.udp.AnimeRecord;
import net.landora.silvideo.anidb.model.Anime;
import net.landora.silvideo.anidb.model.AnimeCategory;
import net.landora.silvideo.anidb.model.AnimeCategoryWeight;
import net.landora.silvideo.anidb.model.AnimeEpisode;
import net.landora.silvideo.anidb.model.AnimeFile;
import net.landora.silvideo.anidb.model.AnimeGroup;
import net.landora.silvideo.anidb.model.AnimeMessage;
import net.landora.silvideo.anidb.model.AnimeName;
import net.landora.silvideo.anidb.model.AnimeNameLookup;
import net.landora.silvideo.anidb.model.AnimeNotification;
import net.landora.silvideo.anidb.model.AnimeRelation;
import net.landora.silvideo.anidb.model.RelationType;
import net.landora.silvideo.anidb.model.repo.AnimeCategoryRepository;
import net.landora.silvideo.anidb.model.repo.AnimeFileRepository;
import net.landora.silvideo.anidb.model.repo.AnimeGroupRepository;
import net.landora.silvideo.anidb.model.repo.AnimeMessageRepository;
import net.landora.silvideo.anidb.model.repo.AnimeNameLookupRepository;
import net.landora.silvideo.anidb.model.repo.AnimeNotificationRepository;
import net.landora.silvideo.anidb.model.repo.AnimeRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import javax.annotation.PostConstruct;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author bdickie
 */
@Service
public class AniDBAPIImpl implements AniDBAPI {

    @Value( "${anidb.http.client-name}" )
    private String HTTP_CLIENT_NAME;
    @Value( "${anidb.http.client-version}" )
    private String HTTP_CLIENT_VER;

    private static final int HTTP_API_PROTO_VERSION = 1;
    private String httpUrl;
    private static final String HTTP_NAMES_URL = "http://anidb.net/api/animetitles.xml.gz";

    private final Logger log = LoggerFactory.getLogger( getClass() );

    @Autowired
    private AnimeTitleParser animeTitleParser;

    @Autowired
    private AnimeCategoryParser animeCategoryParser;

    @Autowired
    private AnimeNameLookupRepository nameLookupRepository;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private AnimeCategoryRepository categoryRepository;

    @Autowired
    private AnimeNotificationRepository notificationRepository;

    @Autowired
    private AnimeMessageRepository messageRepository;

    @Autowired
    private AnimeGroupRepository groupRepository;

    @Autowired
    private AnimeFileRepository fileRepository;

    @Autowired
    private AniDBUDPManager udpManager;

    @Autowired
    private AniDBInfo info;

    public AniDBAPIImpl() {
    }

    @PostConstruct
    public void init() {
        httpUrl = "http://api.anidb.net:9001/httpapi?client=" + HTTP_CLIENT_NAME + "&clientver=" + HTTP_CLIENT_VER + "&protover=" + HTTP_API_PROTO_VERSION;
    }

    @Override
    public boolean downloadTitles() {
        try ( InputStream input = new BufferedInputStream( new URL( HTTP_NAMES_URL ).openStream() ) ) {
//            InputStream is = new FileInputStream( "/home/bdickie/animetitles.xml.gz" );

            animeTitleParser.parseAnimeTitle( new GZIPInputStream( input ) );
            return true;
        } catch ( XMLStreamException | IOException e ) {
            log.error( "Error reading anime names.", e );
            return false;
        }
    }

    @Override
    public Anime downloadAnime( int animeId ) {
        AnimeRecord record = getAnimeRecord( animeId );
        if ( record == null ) {
            return null;
        }

        Anime anime = animeRepository.findByAnimeId( animeId );
        if ( anime == null ) {
            anime = new Anime();
            anime.setAnimeId( animeId );
        }

        anime.setDescription( getAnimeDescription( animeId ) );
        anime.setEndDate( record.getEndDate() );
        anime.setEpisodeCount( record.getEpisodes() > 0 ? record.getEpisodes() : null );
        anime.setHentai( record.isAdultOnly() );
        anime.setLastLoaded( DateTime.now() );
        anime.setNames( Collections.EMPTY_LIST );
        anime.setPictureFileName( record.getPicName() );
        anime.setStartDate( record.getAirDate() );
        anime.setType( record.getType() );

        if ( record.getRatingCount() > 0 ) {
            anime.setRatingPermanent( record.getRating() / 100f );
            anime.setRatingPermanentVotes( record.getRatingCount() );
        }

        if ( record.getTempRatingCount() > 0 ) {
            anime.setRatingTemporary( record.getTempRating() / 100f );
            anime.setRatingTemporaryVotes( record.getTempRatingCount() );
        }

        List<AnimeCategoryWeight> categories = new ArrayList<AnimeCategoryWeight>();
        for ( AnimeRecord.Category cat : record.getCategory() ) {
            AnimeCategory category = categoryRepository.findByCategoryId( cat.getCategoryId() );
            if ( category == null ) {
                category = new AnimeCategory();
                category.setCategoryId( cat.getCategoryId() );
                category = categoryRepository.save( category );
            }
            AnimeCategoryWeight weight = new AnimeCategoryWeight();
            weight.setCategory( category );
            weight.setWeight( cat.getWeight() );
            categories.add( weight );
        }
        anime.setCategories( categories );

        List<AnimeNameLookup> lookupNames = nameLookupRepository.findByAnimeId( animeId );
        List<AnimeName> names = new ArrayList<>( lookupNames.size() );
        for ( AnimeNameLookup lookup : lookupNames ) {
            AnimeName name = new AnimeName();
            name.setLanguage( lookup.getLanguage() );
            name.setName( lookup.getName() );
            name.setType( lookup.getType() );
            names.add( name );
        }

        for ( AnimeName name : names ) {
            if ( name.getType().equalsIgnoreCase( "main" ) ) {
                anime.setNameMain( name.getName() );
            } else if ( name.getType().equalsIgnoreCase( "official" ) && name.getLanguage().equalsIgnoreCase( "en" ) ) {
                anime.setNameEnglish( name.getName() );
            }
        }
        anime.setNames( names );

        List<AnimeRelation> relations = new ArrayList<>();
        for ( AnimeRecord.Relation r : record.getRelatedAnime() ) {
            AnimeRelation relation = new AnimeRelation();
            relation.setRelatedAnimeId( r.getAid() );
            relation.setRelationType( r.getType() );
            relations.add( relation );
        }
        anime.setRelations( relations );
        return anime;
    }

    public String getAnimeDescription( int animeId ) {

        StringBuilder buffer = new StringBuilder();

        int parts = 1; // There will be at least 1 part, once we load the first part was can find the number of parts.
        for ( int part = 0; part < parts; part++ ) {

            AniDBCommand call = new AniDBCommand( "ANIMEDESC" );
            call.addArgument( "aid", animeId );
            call.addArgument( "part", part );

            AniDBReply reply = udpManager.sendData( call );
            if ( part == 0 && reply.getReturnCode() == 333 ) {
                return null;
            }
            if ( reply.getReturnCode() != 233 ) {
                throw new AniDBConnectionError( reply );
            }

            parts = reply.getFirstValueAsInt( 1 );

            buffer.append( reply.getFirstValue( 2 ) );

        }

        return buffer.toString();
    }

    private AnimeRecord getAnimeRecord( int animeId ) {

        AniDBCommand command = new AniDBCommand( "ANIME" );
        command.addArgument( "aid", animeId );
        command.addArgument( "amask", "1D00FBF10100F8" );
        //command.addArgument("amask", "3C00FAF1D100F8");

        AniDBReply reply = udpManager.sendData( command );
        if ( reply.getReturnCode() == 330 ) {
            return null;
        } else if ( reply.getReturnCode() != 230 ) {
            throw new AniDBConnectionError( reply );
        }

        AnimeRecord record = new AnimeRecord();
        record.setAid( animeId );

        AniDBReply.ReplyLine line = reply.getFirstLine();
        int col = 0;

        record.setType( line.getValue( col++ ) );

        String relationIds = line.getValue( col++ );
        String relationTypes = line.getValue( col++ );

        if ( relationIds.length() != 0 ) {
            String[] ids = relationIds.split( "'" );
            String[] types = relationTypes.split( "'" );
            for ( int i = 0; i < Math.min( ids.length, types.length ); i++ ) {
                AnimeRecord.Relation relation = new AnimeRecord.Relation( Integer.parseInt( ids[i] ), RelationType.lookupType( Integer.parseInt( types[i] ) ) );
                record.getRelatedAnime().add( relation );
            }
        }

        String categoryWeights = line.getValue( col++ );

        record.setEpisodes( line.getValueAsInt( col++ ) );
        record.setNormalEpisodes( line.getValueAsInt( col++ ) );
        record.setSpecialEpisodes( line.getValueAsInt( col++ ) );

        record.setAirDate( line.getValueAsDate( col++ ) );
        record.setEndDate( line.getValueAsDate( col++ ) );

        record.setPicName( line.getValue( col++ ) );

        String categoryIds = line.getValue( col++ );
        if ( categoryIds.length() != 0 ) {
            String[] ids = categoryIds.split( "," );
            String[] weights = categoryWeights.split( "," );
            for ( int i = 0; i < Math.min( ids.length, weights.length ); i++ ) {
                AnimeRecord.Category category = new AnimeRecord.Category( Integer.parseInt( ids[i] ), Integer.parseInt( weights[i] ) );
                record.getCategory().add( category );
            }
        }

        record.setRating( line.getValueAsInt( col++ ) );
        record.setRatingCount( line.getValueAsInt( col++ ) );
        record.setTempRating( line.getValueAsInt( col++ ) );
        record.setTempRatingCount( line.getValueAsInt( col++ ) );
        record.setAdultOnly( line.getValueAsBoolean( col++ ) );

        record.setLastUpdated( line.getValueAsInt( col++ ) );

        record.setSpecialsCount( line.getValueAsInt( col++ ) );
        record.setCreditsCount( line.getValueAsInt( col++ ) );
        record.setOtherCount( line.getValueAsInt( col++ ) );
        record.setTrailerCount( line.getValueAsInt( col++ ) );
        record.setParodyCount( line.getValueAsInt( col++ ) );

        return record;
    }

    @Override
    public List<WaitingNotification> getNotifications() {

        AniDBCommand call = new AniDBCommand( "NOTIFYLIST" );

        AniDBReply reply = udpManager.sendData( call );
        if ( reply.getReturnCode() != 291 ) {
            throw new AniDBConnectionError( reply );
        }

        List<WaitingNotification> notifications = new ArrayList<>();
        for ( AniDBReply.ReplyLine line : reply.getLines() ) {
            WaitingNotification notification = new WaitingNotification();
            notification.setId( line.getValueAsInt( 1 ) );
            notification.setType( line.getValue( 0 ).equalsIgnoreCase( "M" ) ? WaitingNotification.Type.Message : WaitingNotification.Type.Notication );
            notifications.add( notification );
        }

        return notifications;
    }

    public boolean hasNotifications() {

        AniDBCommand call = new AniDBCommand( "NOTIFY" );

        AniDBReply reply = udpManager.sendData( call );
        if ( reply.getReturnCode() != 290 ) {
            throw new AniDBConnectionError( reply );
        }
        return reply.getFirstValueAsInt( 0 ) > 0 || reply.getFirstValueAsInt( 1 ) > 0;
    }

    @Override
    public void getNotification( int id ) {

        AniDBCommand call = new AniDBCommand( "NOTIFYGET" );
        call.addArgument( "type", "N" );
        call.addArgument( "id", id );

        AniDBReply reply = udpManager.sendData( call );
        if ( reply.getReturnCode() != 293 ) {
            throw new AniDBConnectionError( reply );
        }

        int typeId = reply.getFirstValueAsInt( 1 );
        DateTime date = reply.getFirstLine().getValueAsDateTime( 3 );

        boolean allSaved = true;
        for ( String fileIdStr : reply.getFirstValue( 5 ).split( "," ) ) {
            int fileId = Integer.parseInt( fileIdStr );
            AnimeNotification notification = notificationRepository.findByFileId( fileId );
            if ( notification == null ) {
                notification = new AnimeNotification();
                notification.setFile( info.getFile( fileId ) );
                notification.setAddedDate( date );
                notification.setTypeId( typeId );
                notificationRepository.save( notification );
            }
        }

        if ( allSaved ) {
            call = new AniDBCommand( "NOTIFYACK" );
            call.addArgument( "type", "N" );
            call.addArgument( "id", id );
            udpManager.sendData( call );
        }
    }

    @Override
    public void getMessage( int id ) {

        AniDBCommand call = new AniDBCommand( "NOTIFYGET" );
        call.addArgument( "type", "M" );
        call.addArgument( "id", id );

        AniDBReply reply = udpManager.sendData( call );
        if ( reply.getReturnCode() != 292 ) {
            throw new AniDBConnectionError( reply );
        }

        int msgId = reply.getFirstValueAsInt( 0 );
        int typeId = reply.getFirstValueAsInt( 4 );
        DateTime date = reply.getFirstLine().getValueAsDateTime( 3 );
        String fromUser = reply.getFirstValue( 2 );
        String title = reply.getFirstValue( 5 );
        String body = reply.getFirstValue( 6 );

        boolean allSaved = true;
        AnimeMessage msg = messageRepository.findByMessageId( msgId );
        if ( msg == null ) {
            msg = new AnimeMessage();
            msg.setMessageId( msgId );
            msg.setBody( body );
            msg.setDate( date );
            msg.setFromUser( fromUser );
            msg.setTitle( title );
            msg.setTypeId( typeId );

            messageRepository.save( msg );
        }

        if ( allSaved ) {
            call = new AniDBCommand( "NOTIFYACK" );
            call.addArgument( "type", "M" );
            call.addArgument( "id", id );
            udpManager.sendData( call );
        }
    }

    @Override
    public AnimeFile getAnimeFile( int fileId ) {

        AniDBCommand command = new AniDBCommand( "FILE" );
//        command.addArgument("ed2k", "57560987b519c1c62c15cdd9c89affd6");
//        command.addArgument("size", "346918566");
        command.addArgument( "fid", fileId );
        command.addArgument( "fmask", FILE_FMASK );
        command.addArgument( "amask", FILE_AMASK );

        AniDBReply reply = udpManager.sendData( command );
        return handleFileResult( reply );
    }

    @Override
    public AnimeFile getAnimeFile( String ed2k, long size ) {

        AniDBCommand command = new AniDBCommand( "FILE" );
        command.addArgument( "ed2k", ed2k );
        command.addArgument( "size", size );
        command.addArgument( "fmask", FILE_FMASK );
        command.addArgument( "amask", FILE_AMASK );

        AniDBReply reply = udpManager.sendData( command );
        return handleFileResult( reply );
    }

    private static final String FILE_FMASK = "71C04B0000";
    private static final String FILE_AMASK = "000000C1";

    private AnimeFile handleFileResult( AniDBReply reply ) {
        if ( reply.getReturnCode() == 320 ) {
            return null;
        } else if ( reply.getReturnCode() != 220 ) {
            throw new AniDBConnectionError( reply );
        }

        AniDBReply.ReplyLine line = reply.getFirstLine();
        int col = 0;

        int fileId = line.getValueAsInt( col++ );

        AnimeFile file = fileRepository.findByFileId( fileId );
        if ( file == null ) {
            file = new AnimeFile();
            file.setFileId( fileId );
        }

        int animeId = line.getValueAsInt( col++ );
        int episodeId = line.getValueAsInt( col++ );

        AnimeEpisode episode = info.getEpisode( episodeId );
        file.setEpisode( episode );

        int groupId = line.getValueAsInt( col++ );

        file.setState( line.getValueAsInt( col++ ) );

        long length = line.getValueAsLong( col++ );
        String ed2k = line.getValue( col++ );
        if ( length > 0 ) {
            file.setSize( length );
            file.setEd2k( ed2k );
            file.setGeneric( false );
        } else {
            file.setGeneric( true );
            file.setSize( null );
            file.setEd2k( null );
        }

        file.setSource( line.getValue( col++ ) );
        file.setVideoCodec( line.getValue( col++ ) );
        file.setVideoResolution( line.getValue( col++ ) );
        file.setFileType( line.getValue( col++ ) );

        String longGroupName = line.getValue( col++ );
        String shortGroupName = line.getValue( col++ );

        DateTime animeLastUpdates = line.getValueAsDateTime( col++ );

        if ( groupId > 0 ) {
            AnimeGroup group = groupRepository.findByGroupId( groupId );
            if ( group == null ) {
                group = new AnimeGroup();
                group.setGroupId( groupId );
                group.setLongName( longGroupName );
                group.setShortName( shortGroupName );
                group = groupRepository.save( group );
            }
            file.setGroup( group );
        }

        return file;
    }

    @Override
    public AnimeEpisode getAnimeEpisode( int episodeId ) {

        AniDBCommand command = new AniDBCommand( "EPISODE" );
        command.addArgument( "eid", episodeId );

        AniDBReply reply = udpManager.sendData( command );
        return handleEpisodeResult( reply );
    }

    @Override
    public AnimeEpisode getAnimeEpisode( int animeId, int episodeNumber ) {

        AniDBCommand command = new AniDBCommand( "EPISODE" );
        command.addArgument( "aid", animeId );
        command.addArgument( "epno", episodeNumber );

        AniDBReply reply = udpManager.sendData( command );
        return handleEpisodeResult( reply );
    }

    @Override
    public AnimeEpisode getAnimeEpisode( int animeId, String episodeNumber ) {

        AniDBCommand command = new AniDBCommand( "EPISODE" );
        command.addArgument( "aid", animeId );
        command.addArgument( "epno", episodeNumber );

        AniDBReply reply = udpManager.sendData( command );
        return handleEpisodeResult( reply );
    }

    private AnimeEpisode handleEpisodeResult( AniDBReply reply ) {
        if ( reply.getReturnCode() == 340 ) {
            return null;
        } else if ( reply.getReturnCode() != 240 ) {
            throw new AniDBConnectionError( reply );
        }

        AniDBReply.ReplyLine line = reply.getFirstLine();
        int col = 0;

        AnimeEpisode episode = new AnimeEpisode();

        episode.setEpisodeId( line.getValueAsInt( col++ ) );
        episode.setAnime( info.getAnime( line.getValueAsInt( col++ ) ) );

        int length = line.getValueAsInt( col++ );
        episode.setLength( length > 0 ? length : null );

        int rating = line.getValueAsInt( col++ );
        int ratingCount = line.getValueAsInt( col++ );
        if ( ratingCount > 0 ) {
            episode.setRating( rating / 100f );
            episode.setRatingVotes( ratingCount );
        }

        episode.setEpisodeNumber( line.getValue( col++ ).trim() );
        episode.setNameEnglish( line.getValue( col++ ) );
        episode.setNameRomaji( line.getValue( col++ ) );
        episode.setNameKanji( line.getValue( col++ ) );

        episode.setAirDate( line.getValueAsDate( col++ ) );

        return episode;
    }

    @Override
    public boolean queueMyListExport( String templateName ) {

        AniDBCommand call = new AniDBCommand( "MYLISTEXPORT" );
        call.addArgument( "template", templateName );

        AniDBReply reply = udpManager.sendData( call );
        switch ( reply.getReturnCode() ) {
            case 217:
                return true;
            case 318:
                return false;
            default:
                throw new AniDBConnectionError( reply );
        }
    }

}
