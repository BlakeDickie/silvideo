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

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author bdickie
 */
@Document
public class AnimeCategory implements java.io.Serializable {

    private String id;

    private int categoryId;
    @DBRef
    private AnimeCategory parentCategory;
    private boolean hentai;
    private String name;
    private String description;

    public AnimeCategory() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public boolean isHentai() {
        return hentai;
    }

    public void setHentai( boolean hentai ) {
        this.hentai = hentai;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId( int categoryId ) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public AnimeCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory( AnimeCategory parentCategory ) {
        this.parentCategory = parentCategory;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final AnimeCategory other = (AnimeCategory) obj;
        if ( this.categoryId != other.categoryId ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.categoryId;
        return hash;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

}
