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
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author bdickie
 */
public class AniDBReply {

    private static final Pattern STATUS_LINE_PATTERN = Pattern.compile( "(\\d{3}) (.*)" );
    private static final Pattern DATA_LINE_PATTERN = Pattern.compile( "<br />|`|\\|", Pattern.CASE_INSENSITIVE );

    private int returnCode;
    private String returnMessage;
    private List<ReplyLine> lines;

    public AniDBReply( String message ) {
        try {
            BufferedReader reader = new BufferedReader( new StringReader( ( message ) ) );

            String statusLine = reader.readLine();
            if ( statusLine == null ) {
                throw new IllegalArgumentException( "AniDB Reply is empty." );
            }
            Matcher m = STATUS_LINE_PATTERN.matcher( statusLine );
            if ( !m.matches() ) {
                throw new IllegalArgumentException( "AniDB Reply has invalid status line: " + statusLine );
            }

            returnCode = Integer.parseInt( m.group( 1 ) );
            returnMessage = m.group( 2 );
            lines = new ArrayList<ReplyLine>();

            String line;
            while ( ( line = reader.readLine() ) != null ) {
                lines.add( new ReplyLine( line ) );
            }
        } catch ( IOException e ) {
            throw new AniDBConnectionError( "Error parsing AniDB reply.", e );
        }

    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    @Override
    public String toString() {
        return lines.toString();
    }

    public ReplyLine getFirstLine() {
        if ( lines.isEmpty() ) {
            return null;
        } else {
            return lines.get( 0 );
        }
    }

    public List<ReplyLine> getLines() {
        return lines;
    }

    public String getFirstValue( int index ) {
        ReplyLine first = getFirstLine();
        if ( first == null ) {
            return "";
        } else {
            return first.getValue( index );
        }
    }

    public int getFirstValueAsInt( int index ) {
        return Integer.parseInt( getFirstValue( index ) );
    }

    public long getFirstValueAsLong( int index ) {
        return Long.parseLong( getFirstValue( index ) );
    }

    public boolean getFirstValueAsBoolean( int index ) {
        int value = getFirstValueAsInt( index );
        return value != 0;
    }

    public static class ReplyLine {

        private String line;

        private List<String> data;

        public ReplyLine( String line ) {
            this.line = line;
            data = new ArrayList<String>();
            StringBuffer buffer = new StringBuffer();
            Matcher m = DATA_LINE_PATTERN.matcher( line );
            while ( m.find() ) {
                String match = m.group();
                if ( match.equalsIgnoreCase( "<br />" ) ) {
                    m.appendReplacement( buffer, "\n" );
                } else if ( match.equals( "`" ) ) {
                    m.appendReplacement( buffer, "'" );
                } else if ( match.equals( "|" ) ) {
                    m.appendReplacement( buffer, "" );
                    data.add( buffer.toString() );
                    buffer.setLength( 0 );
                }
            }
            m.appendTail( buffer );
            data.add( buffer.toString() );
        }

        public String getValue( int index ) {
            if ( data.size() <= index ) {
                return "";
            } else {
                return data.get( index );
            }
        }

        public int getValueAsInt( int index ) {
            return Integer.parseInt( getValue( index ) );
        }

        public long getValueAsLong( int index ) {
            return Long.parseLong( getValue( index ) );
        }

        public boolean getValueAsBoolean( int index ) {
            int value = getValueAsInt( index );
            return value != 0;
        }

        public DateTime getValueAsDateTime( int index ) {
            int value = getValueAsInt( index );
            if ( value == 0 ) {
                return null;
            }

            return new DateTime( value * 1000l );
        }

        public LocalDate getValueAsDate( int index ) {
            int value = getValueAsInt( index );
            if ( value == 0 ) {
                return null;
            }
            Calendar cal = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
            cal.setTimeInMillis( value * 1000l );

            return LocalDate.fromCalendarFields( cal );
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }
}
