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
package net.landora.model.anidb;

/**
 *
 * @author bdickie
 */
public enum AnimeListFileState {

    Normal( 0 ),
    Corrupted( 1 ),
    SelfEdited( 2 ),
    SelfRipped( 10 ),
    OnDVD( 11, "On DVD" ),
    OnVHS( 12, "On VHS" ),
    OnTV( 13, "On TV" ),
    InTheaters( 14 ),
    Streamed( 15 ),
    Other( 100 );

//    0 - unknown - state is unknown or the user doesn't want to provide this information
// 1 - on hdd - the file is stored on hdd (but is not shared)
// 2 - on cd - the file is stored on cd
// 3 - deleted - the file has been deleted or is not available for other reasons (i.e. reencoded)
    private int stateId;
    private String name;

    private AnimeListFileState( int stateId, String name ) {
        this.stateId = stateId;
        this.name = name;
    }

    private AnimeListFileState( int stateId ) {
        this.stateId = stateId;
        StringBuilder buffer = new StringBuilder();
        name = name();
        for ( int i = 0; i < name.length(); i++ ) {
            char c = name.charAt( i );
            if ( i > 0 && java.lang.Character.isUpperCase( c ) ) {
                buffer.append( " " );
            }
            buffer.append( c );
        }
        name = buffer.toString();
    }

    public String getName() {
        return name;
    }

    public int getStateId() {
        return stateId;
    }

    public static AnimeListFileState lookupType( int id ) {

        for ( AnimeListFileState type : values() ) {
            if ( type.getStateId() == id ) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }
}
