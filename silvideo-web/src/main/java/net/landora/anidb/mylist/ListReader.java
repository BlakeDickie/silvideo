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
package net.landora.anidb.mylist;

import net.landora.model.anidb.Anime;
import net.landora.model.anidb.AnimeCategory;
import net.landora.model.anidb.AnimeCategoryWeight;
import net.landora.model.anidb.AnimeEpisode;
import net.landora.model.anidb.AnimeFile;
import net.landora.model.anidb.AnimeGroup;
import net.landora.model.anidb.AnimeListItem;
import net.landora.model.anidb.AnimeName;
import net.landora.model.anidb.repo.AnimeCategoryRepository;
import net.landora.model.anidb.repo.AnimeFileRepository;
import net.landora.model.anidb.repo.AnimeGroupRepository;
import net.landora.model.anidb.repo.AnimeRepository;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xeustechnologies.jtar.TarEntry;
import org.xeustechnologies.jtar.TarInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author bdickie
 */
@Component
public class ListReader {

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private AnimeCategoryRepository animeCategoryRepository;

    @Autowired
    private AnimeGroupRepository animeGroupRepository;

    @Autowired
    private AnimeFileRepository animeFileRepository;

    public ListReader() {
    }

    private Map<String, String> values;

    public boolean download( URL input ) throws Throwable {
        return download( input.openStream() );
    }

    public boolean download( InputStream input ) throws Throwable {
        TarInputStream is = null;
        try {
            is = new TarInputStream( new GZIPInputStream( input ) );

            TarEntry entry;
            while ( ( entry = is.getNextEntry() ) != null ) {
                if ( !entry.getName().equalsIgnoreCase( "mylist.xml" ) ) {
                    continue;
                }

                XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader( is );
                reader.nextTag();
                reader.require( XMLStreamReader.START_ELEMENT, null, "my_anime_list" );
                values = new HashMap<>();
                StringBuilder value = new StringBuilder();

                while ( reader.nextTag() != XMLStreamReader.END_ELEMENT ) {
                    reader.require( XMLStreamReader.START_ELEMENT, null, null );
                    String tableName = reader.getLocalName();

                    values.clear();

                    while ( reader.nextTag() != XMLStreamReader.END_ELEMENT ) {
                        String valueName = reader.getLocalName();

                        value.setLength( 0 );
                        while ( reader.next() != XMLStreamReader.END_ELEMENT ) {
                            switch ( reader.getEventType() ) {
                                case XMLStreamReader.CDATA:
                                case XMLStreamReader.CHARACTERS:
                                case XMLStreamReader.SPACE:
                                    value.append( reader.getText() );
                            }
                        }
                        reader.require( XMLStreamReader.END_ELEMENT, null, valueName );
                        values.put( valueName, value.toString() );

                    }
                    reader.require( XMLStreamReader.END_ELEMENT, null, tableName );

                    handleTable( tableName );

                }
                reader.require( XMLStreamReader.END_ELEMENT, null, "my_anime_list" );

                saveLast();
            }
            return true;
        } finally {
            if ( is != null ) {
                IOUtils.closeQuietly( is );
            } else if ( input != null ) {
                IOUtils.closeQuietly( input );
            }
        }
    }

    private void saveLast() {
        saveLastAnime();
        saveLastEpisode();
        saveLastFile();
    }

    private DateTime exportDate;

    private void handleTable( String tableName ) throws ParseException {
        System.out.println( tableName );
        if ( tableName.equals( "anime" ) ) {
            handleAnime();
        } else if ( tableName.equals( "title" ) ) {
            handleTitle();
        } else if ( tableName.equals( "anime_category" ) ) {
            handleAnimeCategory();
        } else if ( tableName.equals( "episode" ) ) {
            handleEpisode();
        } else if ( tableName.equals( "file" ) ) {
            handleFile();
        } else if ( tableName.equals( "user_info" ) ) {
            exportDate = getDateTime( "ExportDate" );
        }
    }

    private String getString( String name ) {
        String value = values.get( name );
        if ( value == null ) {
            return null;
        }
        return value.replaceAll( Pattern.quote( "<br />" ), "\n" );
    }

    private Integer getInt( String name ) {
        String value = getString( name );
        if ( value == null || value.length() == 0 || value.equals( "-" ) ) {
            return null;
        }
        return Integer.parseInt( value );
    }

    private Long getLong( String name ) {
        String value = getString( name );
        if ( value == null || value.length() == 0 || value.equals( "-" ) ) {
            return null;
        }
        return Long.parseLong( value );
    }

    private Float getFloat( String name ) {
        String value = getString( name );
        if ( value == null || value.length() == 0 || value.equals( "-" ) ) {
            return null;
        }
        return Float.parseFloat( value );
    }

    private Boolean getBoolean( String name ) {
        String value = getString( name );
        if ( value == null || value.length() == 0 || value.equals( "-" ) ) {
            return null;
        }
        return getString( name ).equals( "1" );
    }

    private final DateTimeFormatter dateTimeFormat;
    private final DateTimeFormatter dateFormat;

    {
        dateTimeFormat = DateTimeFormat.forPattern( "dd.MM.yyyy HH:mm" ).withZoneUTC();

        dateFormat = DateTimeFormat.forPattern( "dd.MM.yyyy" ).withZoneUTC();
    }

    public DateTime getDateTime( String name ) throws ParseException {
        String value = getString( name );
        if ( value == null || value.equals( "0" ) || value.length() == 0 || value.equals( "-" ) || value.equals( "?" ) ) {
            return null;
        }

        DateTime date;

        try {
            date = dateTimeFormat.parseDateTime( value );
        } catch ( IllegalArgumentException e ) {
            date = dateFormat.parseDateTime( value );
        }

        return date;
    }

    public LocalDate getLocalDate( String name ) throws ParseException {
        String value = getString( name );
        if ( value == null || value.equals( "0" ) || value.length() == 0 || value.equals( "-" ) || value.equals( "?" ) ) {
            return null;
        }

        LocalDate date;

        try {
            date = dateTimeFormat.parseLocalDate( value );
        } catch ( IllegalArgumentException e ) {
            date = dateFormat.parseLocalDate( value );
        }

        return date;
    }

    private Anime anime;

    private void handleAnime() throws ParseException {
        saveLast();

        int animeId = getInt( "AnimeID" );
        anime = animeRepository.findByAnimeId( animeId );
        if ( anime == null ) {
            anime = new Anime();
            anime.setAnimeId( animeId );
        }

        anime.getNames().clear();
        anime.getCategories().clear();
        anime.setLastLoaded( exportDate );

        anime.setNameEnglish( getString( "NameEnglish" ) );
        if ( anime.getNameEnglish().length() == 0 ) {
            anime.setNameEnglish( null );
        }
        anime.setNameMain( getString( "Name" ) );
        anime.setDescription( getString( "AnimeDescription" ) );
        anime.setStartDate( getLocalDate( "StartDate" ) );
        anime.setEndDate( getLocalDate( "EndDate" ) );
        anime.setEpisodeCount( getInt( "Eps" ) );
        anime.setType( getString( "TypeName" ) );
        anime.setRatingPermanent( getFloat( "Rating" ) );
        anime.setRatingPermanentVotes( anime.getRatingPermanent() == null ? null : getInt( "Votes" ) );
        anime.setRatingTemporary( getFloat( "TempRating" ) );
        anime.setRatingTemporaryVotes( anime.getRatingTemporary() == null ? null : getInt( "TempVotes" ) );
        anime.setHentai( getBoolean( "Hentai" ) );

    }

    private void handleTitle() throws ParseException {
        if ( anime == null ) {
            return;
        }

        AnimeName name = new AnimeName();
        String language = getString( "LanguageName" );
        String converted = LANGUAGE_CONVERT.get( language );

        name.setLanguage( converted == null ? language : converted );
        name.setType( getString( "TitleTypeName" ) );
        name.setName( getString( "Name" ) );

        anime.getNames().add( name );

    }

    private void handleAnimeCategory() throws ParseException {
        if ( anime == null ) {
            return;
        }

        int categoryId = getInt( "CategoryID" );
        AnimeCategory category = animeCategoryRepository.findByCategoryId( categoryId );
        if ( category == null ) {
            category = new AnimeCategory();
            category.setCategoryId( categoryId );
            category.setName( getString( "CategoryName" ) );
            category.setHentai( getBoolean( "CategoryHentai" ) );
            category = animeCategoryRepository.save( category );
        }

        AnimeCategoryWeight categoryWeight = new AnimeCategoryWeight();
        categoryWeight.setCategory( category );
        categoryWeight.setWeight( getInt( "CategoryWeight" ) / 100 );

        anime.getCategories().add( categoryWeight );

    }

    private void saveLastAnime() {
        if ( anime == null ) {
            return;
        }
        animeRepository.save( anime );
        anime = null;
    }

    private final static Map<String, String> LANGUAGE_CONVERT;

    static {
        LANGUAGE_CONVERT = new HashMap<>();

        LANGUAGE_CONVERT.put( "english", "en" );
        LANGUAGE_CONVERT.put( "japanese", "ja" );
        LANGUAGE_CONVERT.put( "japanese (transcription)", "x-jat" );
        LANGUAGE_CONVERT.put( "spanish", "es" );
        LANGUAGE_CONVERT.put( "arabic", "ar" );
        LANGUAGE_CONVERT.put( "brazilian portuguese", "pt-BR" );
        LANGUAGE_CONVERT.put( "chinese (simplified)", "zh-Hans" );

    }

    private AnimeEpisode episode;

    private void handleEpisode() throws ParseException {
        saveLast();

        int animeId = getInt( "AnimeID" );
        int episodeId = getInt( "EpID" );

        Anime a = animeRepository.findByAnimeId( animeId );
        for ( AnimeEpisode e : a.getEpisodes() ) {
            if ( e.getEpisodeId() == episodeId ) {
                episode = e;
                break;
            }
        }

        if ( episode == null ) {
            episode = new AnimeEpisode();
            episode.setEpisodeId( episodeId );
            episode.setAnime( a );
        }

        episode.setLength( getInt( "EpLength" ) );
        episode.setRating( getFloat( "EpRating" ) );
        episode.setRatingVotes( episode.getRating() == null ? null : getInt( "EpVotes" ) );
        episode.setNameEnglish( getString( "EpName" ) );
        episode.setNameKanji( getString( "EpNameKanji" ) );
        episode.setNameRomaji( getString( "EpNameRomaji" ) );
        episode.setAirDate( getLocalDate( "EpAired" ) );
        episode.setEpisodeNumber( getString( "EpNo" ) );

        a.getEpisodes().add( episode );
    }

    private void saveLastEpisode() {
        if ( episode == null ) {
            return;
        }

        animeRepository.save( episode.getAnime() );
        episode = null;
    }

    private AnimeFile file;
    private AnimeListItem listItem;

    private void handleFile() throws ParseException {
        saveLast();

        int fileId = getInt( "FID" );

        file = animeFileRepository.findByFileId( fileId );
        if ( file == null ) {
            file = new AnimeFile();
            file.setFileId( fileId );
            Anime a = animeRepository.findByEpisodes_EpisodeId( getInt( "EpID" ) );
            file.setEpisode( a.findEpisodeById( getInt( "EpID" ) ) );
        }

        boolean generic = getBoolean( "Generic" );
        file.setGeneric( generic );

        if ( !generic ) {

            int groupId = getInt( "GID" );
            if ( groupId > 0 ) {
                AnimeGroup group = animeGroupRepository.findByGroupId( groupId );
                if ( group == null ) {
                    group = new AnimeGroup();
                    group.setGroupId( groupId );
                    group.setLongName( getString( "GName" ) );
                    group.setShortName( getString( "GShortName" ) );
                    group = animeGroupRepository.save( group );
                }
                file.setGroup( group );
            }

            file.setEd2k( getString( "ed2kHash" ) );
            file.setSize( getLong( "Size" ) );

            int state = getInt( "State" );

            if ( ( state & 1 ) != 0 ) {
                file.setCrcValid( true );
            } else if ( ( state & 2 ) != 0 ) {
                file.setCrcValid( false );
            }

            file.setVersion( 1 );
            if ( ( state & 4 ) != 0 ) {
                file.setVersion( 2 );
            } else if ( ( state & 8 ) != 0 ) {
                file.setVersion( 3 );
            } else if ( ( state & 16 ) != 0 ) {
                file.setVersion( 4 );
            } else if ( ( state & 32 ) != 0 ) {
                file.setVersion( 5 );
            }

            if ( ( state & 64 ) != 0 ) {
                file.setCensored( false );
            } else if ( ( state & 128 ) != 0 ) {
                file.setCensored( false );
            }

            file.setVideoResolution( getString( "ResName" ) );
            file.setFileType( getString( "FileType" ) );
            file.setVideoCodec( getString( "VCodecName" ) );
            file.setSource( getString( "TypeName" ) );

        } else {
            file.setGroup( null );
            file.setCrcValid( null );
            file.setVersion( null );
            file.setCensored( null );
            file.setEd2k( null );
            file.setSize( null );
            file.setFileType( "" );
            file.setVideoResolution( "" );
            file.setVideoCodec( "" );
            file.setSource( "" );
        }

        listItem = file.getListItem();
        if ( listItem == null ) {
            listItem = new AnimeListItem();
            file.setListItem( listItem );
        }

        listItem.setStateId( getInt( "MyState" ) );
        listItem.setFileStateId( getInt( "MyFileState" ) );
        listItem.setViewDate( getDateTime( "ViewDate" ) );
        listItem.setAddedDate( getDateTime( "ListDate" ) );
        listItem.setStorage( getString( "Storage" ) );
        listItem.setSource( getString( "Source" ) );
        listItem.setOther( getString( "Other" ) );
    }

    private void saveLastFile() {
        if ( file == null ) {
            return;
        }

        file = animeFileRepository.save( file );

        file = null;
        listItem = null;
    }

}
