/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author bdickie
 */
@Document
public class AnimeMapping {

    private String id;

    @Indexed
    private int anidbId;
    private Long tvdbId;
    private String tmdbId;
    private String imdbId;
    private Long defaultTvdbSeason;

    public AnimeMapping() {
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public int getAnidbId() {
        return anidbId;
    }

    public void setAnidbId( int anidbId ) {
        this.anidbId = anidbId;
    }

    public Long getTvdbId() {
        return tvdbId;
    }

    public void setTvdbId( Long tvdbId ) {
        this.tvdbId = tvdbId;
    }

    public String getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId( String tmdbId ) {
        this.tmdbId = tmdbId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId( String imdbId ) {
        this.imdbId = imdbId;
    }

    public Long getDefaultTvdbSeason() {
        return defaultTvdbSeason;
    }

    public void setDefaultTvdbSeason( Long defaultTvdbSeason ) {
        this.defaultTvdbSeason = defaultTvdbSeason;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.anidbId;
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
        final AnimeMapping other = (AnimeMapping) obj;
        if ( this.anidbId != other.anidbId ) {
            return false;
        }
        return true;
    }

}
