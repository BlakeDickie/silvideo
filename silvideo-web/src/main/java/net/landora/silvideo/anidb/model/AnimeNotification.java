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

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author bdickie
 */
@Document
public class AnimeNotification implements java.io.Serializable {

    private String id;

    @DBRef
    private AnimeFile file;
    private Integer fileId;
    private DateTime addedDate;
    private DateTime viewDate;
    private DateTime removedDate;
    private NotificationType type;

    public AnimeNotification() {
    }

    public DateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate( DateTime addedDate ) {
        this.addedDate = addedDate;
    }

    public AnimeFile getFile() {
        return file;
    }

    public void setFile( AnimeFile file ) {
        this.file = file;
        if ( file != null ) {
            this.fileId = file.getFileId();
        } else {
            this.fileId = null;
        }
    }

    public DateTime getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate( DateTime removedDate ) {
        this.removedDate = removedDate;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType( NotificationType type ) {
        this.type = type;
    }

    public DateTime getViewDate() {
        return viewDate;
    }

    public void setViewDate( DateTime viewDate ) {
        this.viewDate = viewDate;
    }

    public int getTypeId() {
        return getType().getType();
    }

    public void setTypeId( int typeId ) {
        setType( NotificationType.lookupType( typeId ) );
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId( Integer fileId ) {
        this.fileId = fileId;
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    @Override
    public String toString() {
        return file.toString();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final AnimeNotification other = (AnimeNotification) obj;
        if ( this.file != other.file && ( this.file == null || !this.file.equals( other.file ) ) ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + ( this.file != null ? this.file.hashCode() : 0 );
        return hash;
    }

}
