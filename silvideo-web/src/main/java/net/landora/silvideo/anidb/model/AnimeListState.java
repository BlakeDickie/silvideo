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
package net.landora.silvideo.anidb.model;

/**
 *
 * @author bdickie
 */
public enum AnimeListState {

    Unknown( 0 ),
    HDD( 1 ),
    CD( 2 ),
    Deleted( 3 );

//    0 - unknown - state is unknown or the user doesn't want to provide this information
// 1 - on hdd - the file is stored on hdd (but is not shared)
// 2 - on cd - the file is stored on cd
// 3 - deleted - the file has been deleted or is not available for other reasons (i.e. reencoded)
    private int stateId;
    private String name;

    private AnimeListState( int stateId, String name ) {
        this.stateId = stateId;
        this.name = name;
    }

    private AnimeListState( int stateId ) {
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

    public static AnimeListState lookupType( int id ) {

        for ( AnimeListState type : values() ) {
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
