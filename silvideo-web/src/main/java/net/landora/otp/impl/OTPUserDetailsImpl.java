/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.otp.impl;

import net.landora.otp.OTPUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 *
 * @author Blake Dickie
 */
public class OTPUserDetailsImpl implements OTPUserDetails, Serializable {

    private final User user;

    public OTPUserDetailsImpl( User user ) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public byte[] getEncryptedTotpSecretKey() {
        return user.getTotpKey();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList( new SimpleGrantedAuthority( "ROLE_USER" ) );
    }

    @Override
    public String getPassword() {
        return user.getHashedPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode( this.user );
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
        final OTPUserDetailsImpl other = (OTPUserDetailsImpl) obj;
        if ( !Objects.equals( this.user, other.user ) ) {
            return false;
        }
        return true;
    }


}
