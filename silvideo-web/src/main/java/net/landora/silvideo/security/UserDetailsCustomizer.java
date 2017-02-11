/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author bdickie
 */
public class UserDetailsCustomizer implements AuthoritiesExtractor {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public List<GrantedAuthority> extractAuthorities( Map<String, Object> map ) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        String subject = string( map.get( "sub" ) );

        UserInfo userInfo = userInfoRepository.findByGoogleSubject( subject );

        if ( userInfo == null ) {
            userInfo = new UserInfo();
            userInfo.setGoogleSubject( subject );
        }

        userInfo.setName( string( map.get( "name" ) ) );
        userInfo.setFirstName( string( map.get( "given_name" ) ) );
        userInfo.setLastName( string( map.get( "family_name" ) ) );

        userInfo = userInfoRepository.save( userInfo );

        if ( userInfo.isApproved() ) {
            authorities.add( new SimpleGrantedAuthority( "ROLE_APPROVED" ) );

            if ( userInfo.isAdmin() ) {
                authorities.add( new SimpleGrantedAuthority( "ROLE_ADMIN" ) );
            }
        }

        return authorities;

    }

    private static String string( Object value ) {
        if ( value == null ) {
            return null;
        } else {
            return value.toString();
        }
    }

}
