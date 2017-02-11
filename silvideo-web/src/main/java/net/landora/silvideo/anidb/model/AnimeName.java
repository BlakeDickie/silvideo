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
public class AnimeName implements java.io.Serializable {

    private String name;
    private String type;
    private String language;

    public AnimeName() {
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage( String language ) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final AnimeName other = (AnimeName) obj;

        if ( ( this.name == null ) ? ( other.name != null ) : !this.name.equals( other.name ) ) {
            return false;
        }
        if ( ( this.type == null ) ? ( other.type != null ) : !this.type.equals( other.type ) ) {
            return false;
        }
        if ( ( this.language == null ) ? ( other.language != null ) : !this.language.equals( other.language ) ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + ( this.name != null ? this.name.hashCode() : 0 );
        hash = 19 * hash + ( this.type != null ? this.type.hashCode() : 0 );
        hash = 19 * hash + ( this.language != null ? this.language.hashCode() : 0 );
        return hash;
    }

    @Override
    public String toString() {
        return String.format( "%s [%s:%s]", name, language, type );
    }

}
