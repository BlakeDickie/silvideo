/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.model.anidb;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;

/**
 *
 * @author bdickie
 */
@Document
@CompoundIndexes( {
    @CompoundIndex( name = "name_idx", def = "{'language': 1, 'name': 1}" )
} )
public class AnimeNameLookup implements java.io.Serializable {

    private String id;
    @Indexed
    private int animeId;

    private String name;
    private String nameEscaped;
    private String type;
    private String language;

    public AnimeNameLookup() {
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public int getAnimeId() {
        return animeId;
    }

    public void setAnimeId( int animeId ) {
        this.animeId = animeId;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getNameEscaped() {
        return nameEscaped;
    }

    public void setNameEscaped( String nameEscaped ) {
        this.nameEscaped = nameEscaped;
    }

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage( String language ) {
        this.language = language;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.animeId;
        hash = 79 * hash + Objects.hashCode( this.name );
        hash = 79 * hash + Objects.hashCode( this.type );
        hash = 79 * hash + Objects.hashCode( this.language );
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
        final AnimeNameLookup other = (AnimeNameLookup) obj;
        if ( this.animeId != other.animeId ) {
            return false;
        }
        if ( !Objects.equals( this.name, other.name ) ) {
            return false;
        }
        if ( !Objects.equals( this.type, other.type ) ) {
            return false;
        }
        if ( !Objects.equals( this.language, other.language ) ) {
            return false;
        }
        return true;
    }

}
