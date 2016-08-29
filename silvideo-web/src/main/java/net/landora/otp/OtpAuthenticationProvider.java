/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.otp;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Blake Dickie
 */
public class OtpAuthenticationProvider extends DaoAuthenticationProvider {

    public OtpAuthenticationProvider() {
    }

    @Override
    protected Authentication createSuccessAuthentication( Object principal, Authentication authentication, UserDetails user ) {
        PreOtpAuthentication result = new PreOtpAuthentication(                                            principal, authentication.getCredentials(),
                                            user.getAuthorities() );
        result.setDetails( authentication.getDetails() );

        if ( user instanceof OTPUserDetails ) {
            OTPUserDetails otpUserDetails = (OTPUserDetails) user;
            if ( otpUserDetails.getEncryptedTotpSecretKey() == null ) {
                result.passToken();
            }
        } else
            result.passToken();

        return result;
    }
}
