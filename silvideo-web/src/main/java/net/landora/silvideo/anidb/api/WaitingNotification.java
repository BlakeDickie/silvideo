/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.api;

import java.util.Objects;

/**
 *
 * @author bdickie
 */
public class WaitingNotification {

    private Type type;
    private int id;

    public WaitingNotification() {
    }

    public WaitingNotification( Type type, int id ) {
        this.type = type;
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType( Type type ) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode( this.type );
        hash = 97 * hash + this.id;
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
        final WaitingNotification other = (WaitingNotification) obj;
        if ( this.id != other.id ) {
            return false;
        }
        if ( this.type != other.type ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WaitingNotification{" + "type=" + type + ", id=" + id + '}';
    }

    public static enum Type {
        Message,
        Notication;
    }
}
