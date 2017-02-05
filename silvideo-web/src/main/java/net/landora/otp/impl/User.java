/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.otp.impl;

import java.util.Objects;

/**
 *
 * @author Blake Dickie
 */
public class User implements java.io.Serializable {

    private String id;
    private String username;
    private String hashedPassword;
    private byte[] totpKey;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword( String hashedPassword ) {
        this.hashedPassword = hashedPassword;
    }

    public byte[] getTotpKey() {
        return totpKey;
    }

    public void setTotpKey( byte[] totpKey ) {
        this.totpKey = totpKey;
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
        final User other = (User) obj;
        if ( !Objects.equals( this.id, other.id ) ) {
            return false;
        }
        return true;
    }

}
