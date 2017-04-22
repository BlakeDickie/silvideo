/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.model;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;

/**
 *
 * @author bdickie
 */
@Document
public class NfsRoot {

    private String id;

    private String nfsPath;
    private int userId;
    private int groupId;

    public NfsRoot() {
    }

    public String getNfsPath() {
        return nfsPath;
    }

    public void setNfsPath( String nfsPath ) {
        this.nfsPath = nfsPath;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId( int userId ) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId( int groupId ) {
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode( this.id );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final NfsRoot other = (NfsRoot) obj;
        if ( !Objects.equals( this.id, other.id ) ) {
            return false;
        }
        return true;
    }

}
