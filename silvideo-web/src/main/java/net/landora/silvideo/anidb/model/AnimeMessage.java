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
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author bdickie
 */
@Document
public class AnimeMessage implements java.io.Serializable {

    private String id;

    private int messageId;
    private String fromUser;
    private DateTime date;
    private MessageType type;
    private String title;
    private String body;
    private DateTime removedDate;

    public AnimeMessage() {
    }

    public String getBody() {
        return body;
    }

    public void setBody( String body ) {
        this.body = body;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate( DateTime date ) {
        this.date = date;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser( String fromUser ) {
        this.fromUser = fromUser;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId( int messageId ) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public MessageType getType() {
        return type;
    }

    public void setType( MessageType type ) {
        this.type = type;
    }

    public DateTime getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate( DateTime removedDate ) {
        this.removedDate = removedDate;
    }

    public int getTypeId() {
        return getType().getType();
    }

    public void setTypeId( int typeId ) {
        setType( MessageType.lookupType( typeId ) );
    }
}
