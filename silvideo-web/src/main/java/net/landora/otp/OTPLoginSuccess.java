/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.otp;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Blake Dickie
 */
public class OTPLoginSuccess extends SavedRequestAwareAuthenticationSuccessHandler {

    public OTPLoginSuccess() {
    }

    @Override
    public void onAuthenticationSuccess( HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication ) throws ServletException, IOException {
        if ( authentication instanceof PreOtpAuthentication ) {
            PreOtpAuthentication preAuth = (PreOtpAuthentication) authentication;
            if ( !preAuth.isTokenPassed() ) {
                getRedirectStrategy().sendRedirect( request, response, "/otp/auth/start" );
                return;
            }
        }
        super.onAuthenticationSuccess( request, response, authentication );
    }

}
