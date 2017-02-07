/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.security;

import java.util.Objects;

/**
 *
 * @author bdickie
 */
public class UserInfo implements java.io.Serializable {

    private String id;

    private String googleSubject;

    private String name;

    private String firstName;

    private String lastName;

    private boolean approved;

    private boolean admin;

    public UserInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getGoogleSubject() {
        return googleSubject;
    }

    public void setGoogleSubject( String googleSubject ) {
        this.googleSubject = googleSubject;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved( boolean approved ) {
        this.approved = approved;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin( boolean admin ) {
        this.admin = admin;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode( this.id );
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
        final UserInfo other = (UserInfo) obj;
        if ( !Objects.equals( this.id, other.id ) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }

}
