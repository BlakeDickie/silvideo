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
package net.landora.silvideo.anidb.api.impl.http;

import net.landora.silvideo.anidb.model.AnimeNameLookup;
import net.landora.silvideo.anidb.model.repo.AnimeNameLookupRepository;
import net.landora.silvideo.util.XMLUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author bdickie
 */
@Component
public class AnimeTitleParser {

    private final Logger log = LoggerFactory.getLogger( AnimeTitleParser.class );

    @Autowired
    private AnimeNameLookupRepository animeNameLookupSummaryRepository;

    public void parseAnimeTitle( InputStream is ) throws XMLStreamException {

        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader( is );

        reader.nextTag();
        reader.require( XMLStreamReader.START_ELEMENT, null, "animetitles" );

        int t1;
        while ( ( t1 = reader.nextTag() ) != XMLStreamReader.END_ELEMENT ) {
            reader.require( XMLStreamReader.START_ELEMENT, null, "anime" );

            int animeId = Integer.parseInt( reader.getAttributeValue( null, "aid" ) );

            List<AnimeNameLookup> oldLookups = animeNameLookupSummaryRepository.findByAnimeId( animeId );

            List<AnimeNameLookup> existingNames = new ArrayList<>( oldLookups );
            List<AnimeNameLookup> newNames = new ArrayList<>( oldLookups );

            int t2;
            while ( ( t2 = reader.nextTag() ) != XMLStreamReader.END_ELEMENT ) {
                reader.require( XMLStreamReader.START_ELEMENT, null, "title" );

                AnimeNameLookup name = new AnimeNameLookup();
                name.setAnimeId( animeId );
                for ( int i = 0; i < reader.getAttributeCount(); i++ ) {
                    String aname = reader.getAttributeLocalName( i );
                    if ( aname.equals( "type" ) ) {
                        name.setType( reader.getAttributeValue( i ) );
                    } else if ( aname.equals( "lang" ) ) {
                        name.setLanguage( reader.getAttributeValue( i ) );
                    }

                }

                name.setName( XMLUtilities.nextString( reader ) );
                boolean existing = false;
                for ( AnimeNameLookup existingName : existingNames ) {
                    if ( existingName.getName().equals( name.getName() )
                         && existingName.getLanguage().equals( name.getLanguage() )
                         && existingName.getType().equals( name.getType() ) ) {
                        existing = true;
                        existingNames.remove( existingName );
                        break;
                    }
                }
                if ( !existing ) {
                    newNames.add( name );
                }
            }

            animeNameLookupSummaryRepository.delete( existingNames );
            animeNameLookupSummaryRepository.save( newNames );

        }
        reader.close();

    }

}
