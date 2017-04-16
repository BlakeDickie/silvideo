/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.tvdb;

import net.landora.silvideo.anidb.model.AnimeMapping;
import net.landora.silvideo.anidb.model.repo.AnimeMappingRepository;
import net.landora.silvideo.util.XMLUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Pattern;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author bdickie
 */
@Component
public class MappingListLoader {

    private Logger log = LoggerFactory.getLogger( getClass() );

    private Pattern numeric = Pattern.compile( "\\d+" );

    @Value( "${anidb.mapping.url}" )
    private String mappingListUrl;

    @Autowired
    private AnimeMappingRepository repository;

    public boolean loadList() {

        try ( InputStream is = new URL( mappingListUrl ).openStream() ) {

            XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader( is );

            reader.nextTag();
            reader.require( XMLStreamReader.START_ELEMENT, null, "anime-list" );

            while ( reader.nextTag() != XMLStreamReader.END_ELEMENT ) {

                reader.require( XMLStreamReader.START_ELEMENT, null, "anime" );

                int anidbId = Integer.parseInt( reader.getAttributeValue( null, "anidbid" ) );

                AnimeMapping mapping = repository.findByAnidbId( anidbId );
                if ( mapping == null ) {
                    mapping = new AnimeMapping();
                    mapping.setAnidbId( anidbId );
                }

                String tvdbId = reader.getAttributeValue( null, "tvdbid" );
                if ( tvdbId != null && numeric.matcher( tvdbId ).matches() ) {
                    mapping.setTvdbId( Long.parseLong( tvdbId ) );

                    String defaultSeason = reader.getAttributeValue( null, "defaulttvdbseason" );
                    if ( defaultSeason != null && !defaultSeason.equals( "a" ) ) {
                        mapping.setDefaultTvdbSeason( Long.parseLong( defaultSeason ) );
                    }
                }

                mapping.setImdbId( reader.getAttributeValue( null, "imdbid" ) );
                mapping.setTmdbId( reader.getAttributeValue( null, "tmdbid" ) );

                while ( reader.next() != XMLStreamReader.END_ELEMENT ) {
                    if ( reader.getEventType() == XMLStreamReader.START_ELEMENT ) {

                        String tagName = reader.getLocalName();

                        if ( tagName.equals( "before" ) ) {
                            XMLUtilities.ignoreTag( reader );
                        } else {
                            XMLUtilities.ignoreTag( reader );
                        }
                        reader.require( XMLStreamReader.END_ELEMENT, null, tagName );
                    }
                }

                reader.require( XMLStreamReader.END_ELEMENT, null, "anime" );
                repository.save( mapping );
            }

            reader.require( XMLStreamReader.END_ELEMENT, null, "anime-list" );

            return true;
        } catch ( IOException | XMLStreamException e ) {
            log.error( "Error loading mapping list.", e );
            return false;

        }

    }
}
