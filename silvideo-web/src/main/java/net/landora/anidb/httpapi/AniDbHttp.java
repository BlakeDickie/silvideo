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
package net.landora.anidb.httpapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author bdickie
 */
@Component
public class AniDbHttp {

    @Value( "${anidb.http.client-name}" )
    private String HTTP_CLIENT_NAME = "gsvideohttp";
    @Value( "${anidb.http.client-version}" )
    private String HTTP_CLIENT_VER;

    private static final int HTTP_API_PROTO_VERSION = 1;
    private String httpUrl = "http://api.anidb.net:9001/httpapi?client=" + HTTP_CLIENT_NAME + "&clientver=" + HTTP_CLIENT_VER + "&protover=" + HTTP_API_PROTO_VERSION;
    private static final String HTTP_NAMES_URL = "http://anidb.net/api/animetitles.xml.gz";

    private final Logger log = LoggerFactory.getLogger( getClass() );

    @Autowired
    private AnimeTitleParser animeTitleParser;

    public AniDbHttp() {
    }

//    public void updateCategoryNames() {
//        try {
//            InputStream is = new GZIPInputStream(new BufferedInputStream(new URL(String.format("%s&request=categorylist", HTTP_URL)).openStream()));
//            new AnimeCategoryParser().parseAnimeCategory(is);
//        } catch (Exception e) {
//            log.error("Error reading categories.");
//        }
//    }
    public void updateAnimeNames() {
        try {
            InputStream is = new FileInputStream( "/home/bdickie/animetitles.xml.gz" );
            is = new GZIPInputStream( new BufferedInputStream( is ) );
            animeTitleParser.parseAnimeTitle( is );
//            InputStream is = new GZIPInputStream(new BufferedInputStream(new URL(HTTP_NAMES_URL).openStream()));
//            new AnimeTitleParser().parseAnimeTitle(is);
        } catch ( Exception e ) {
            log.error( "Error reading anime names.", e );
        }
    }
}
