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

import net.landora.silvideo.anidb.model.AnimeCategory;
import net.landora.silvideo.anidb.model.repo.AnimeCategoryRepository;
import net.landora.silvideo.util.XMLUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author bdickie
 */
@Component
public class AnimeCategoryParser {

    private Logger log = LoggerFactory.getLogger( AnimeCategoryParser.class );

    @Autowired
    private AnimeCategoryRepository repo;

    public void parseAnimeCategory( InputStream is ) throws XMLStreamException {

        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader( is );

        reader.nextTag();
        reader.require( XMLStreamReader.START_ELEMENT, null, "categorylist" );

        Map<Integer, AnimeCategory> categories = new HashMap<>();
        Map<Integer, Integer> parentCategories = new HashMap<>();

        int t1;
        while ( ( t1 = reader.nextTag() ) != XMLStreamReader.END_ELEMENT ) {
            reader.require( XMLStreamReader.START_ELEMENT, null, "category" );

            int categoryId = Integer.parseInt( reader.getAttributeValue( null, "id" ) );
            int parentCategoryId = Integer.parseInt( reader.getAttributeValue( null, "parentid" ) );

            AnimeCategory category = repo.findByCategoryId( categoryId );
            if ( category == null ) {
                category = new AnimeCategory();
                category.setCategoryId( categoryId );
            }

            category.setHentai( Boolean.parseBoolean( reader.getAttributeValue( null, "ishentai" ) ) );

            int t2;
            while ( ( t2 = reader.nextTag() ) != XMLStreamReader.END_ELEMENT ) {
                if ( reader.getLocalName().equals( "name" ) ) {
                    category.setName( XMLUtilities.nextString( reader ).trim() );
                } else if ( reader.getLocalName().equals( "description" ) ) {
                    category.setDescription( XMLUtilities.nextString( reader ).trim() );
                }
            }

            if ( categoryId > 0 ) {
                category = repo.save( category );

                categories.put( categoryId, category );

                if ( parentCategoryId > 0 ) {
                    parentCategories.put( categoryId, parentCategoryId );
                }
            }
        }
        reader.close();

        for ( Map.Entry<Integer, Integer> entry : parentCategories.entrySet() ) {
            AnimeCategory c1 = categories.get( entry.getKey() );
            c1.setParentCategory( categories.get( entry.getValue() ) );
            categories.put( c1.getCategoryId(), repo.save( c1 ) );
        }

    }

}
