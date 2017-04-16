/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.settings;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;

/**
 *
 * @author bdickie
 */
@Document
public class Settings {

    private String id;

    private String anidbUsername;
    private String anidbPassword;
    private DateTime outstandingAnidbExport;
    private DateTime lastCompletedExport;

    public Settings() {
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getAnidbUsername() {
        return anidbUsername;
    }

    public void setAnidbUsername( String anidbUsername ) {
        this.anidbUsername = anidbUsername;
    }

    public String getAnidbPassword() {
        return anidbPassword;
    }

    public void setAnidbPassword( String anidbPassword ) {
        this.anidbPassword = anidbPassword;
    }

    public DateTime getLastCompletedExport() {
        return lastCompletedExport;
    }

    public void setLastCompletedExport( DateTime lastCompletedExport ) {
        this.lastCompletedExport = lastCompletedExport;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode( this.id );
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
        final Settings other = (Settings) obj;
        if ( !Objects.equals( this.id, other.id ) ) {
            return false;
        }
        return true;
    }

    public DateTime getOutstandingAnidbExport() {
        return outstandingAnidbExport;
    }

    public void setOutstandingAnidbExport( DateTime outstandingAnidbExport ) {
        this.outstandingAnidbExport = outstandingAnidbExport;
    }

}
