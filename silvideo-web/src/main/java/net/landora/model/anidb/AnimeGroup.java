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
public class AnimeGroup implements java.io.Serializable {

    private String id;
    private int groupId;
    private String shortName;
    private String longName;
    private String url;
    private boolean fullyLoaded;

    public AnimeGroup() {
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId( int groupId ) {
        this.groupId = groupId;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName( String longName ) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName( String shortName ) {
        this.shortName = shortName;
    }

    public boolean isFullyLoaded() {
        return fullyLoaded;
    }

    public void setFullyLoaded( boolean fullyLoaded ) {
        this.fullyLoaded = fullyLoaded;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final AnimeGroup other = (AnimeGroup) obj;
        if ( this.groupId != other.groupId ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + this.groupId;
        return hash;
    }

    @Override
    public String toString() {
        return getShortName();
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

}
