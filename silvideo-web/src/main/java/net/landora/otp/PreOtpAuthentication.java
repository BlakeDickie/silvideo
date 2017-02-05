/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.otp;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Blake Dickie
 */
public class PreOtpAuthentication extends UsernamePasswordAuthenticationToken {

    private boolean tokenPassed;

    public PreOtpAuthentication( Object principal, Object credentials ) {
        super( principal, credentials );
    }

    public PreOtpAuthentication( Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities ) {
        super( principal, credentials, authorities );
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if ( tokenPassed ) {
            return super.getAuthorities();
        } else {
            return Arrays.asList( new SimpleGrantedAuthority( "PREOTP" ) );
        }
    }

    public void passToken() {
        tokenPassed = true;
    }

    public boolean isTokenPassed() {
        return tokenPassed;
    }

}
