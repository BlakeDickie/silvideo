/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.otp;

import com.google.common.io.BaseEncoding;
import com.lochbridge.oath.otp.HmacShaAlgorithm;
import com.lochbridge.oath.otp.TOTPValidator;
import com.lochbridge.oath.otp.keyprovisioning.OTPAuthURI;
import com.lochbridge.oath.otp.keyprovisioning.OTPAuthURIBuilder;
import com.lochbridge.oath.otp.keyprovisioning.OTPKey;
import com.lochbridge.oath.otp.keyprovisioning.OTPKey.OTPType;
import com.lochbridge.oath.otp.keyprovisioning.qrcode.QRCodeWriter;
import com.lochbridge.oath.otp.keyprovisioning.qrcode.QRCodeWriter.ErrorCorrectionLevel;
import net.landora.otp.impl.OTPUserDetailsImpl;
import net.landora.otp.impl.User;
import net.landora.otp.impl.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Blake Dickie
 */
@Controller
public class OTPController {

    @Autowired
    private SecureRandom random;

    @Autowired
    private BytesEncryptor encryptor;

    @Autowired
    private UserRepository userRepository;

    @GetMapping( "/otp/auth/start" )
    public String authOtp( Model model ) {
        model.addAttribute( "failed", false );
        return "otp-auth";
    }

    @GetMapping( "/otp/setup" )
    public String setupOtp( Model model ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OTPUserDetailsImpl userDetails = (OTPUserDetailsImpl) authentication.getPrincipal();

        model.addAttribute( "user", userDetails.getUser() );
        model.addAttribute( "optEnabled", userDetails.getEncryptedTotpSecretKey() != null );

        return "otp-setup";
    }

    @PostMapping( "/otp/setup/generate-key" )
    public String generateKey( Model model ) {

        byte[] bytes = new byte[20];
        random.nextBytes( bytes );

        byte[] encryptedBytes = encryptor.encrypt( bytes );

        String encryptString = BaseEncoding.base32().encode( encryptedBytes );

        model.addAttribute( "keyString", encryptString );

        return "otp-new-key";
    }

    @PostMapping( "/otp/setup/accept-key" )
    public String confirmKey( Model model, String key, String token ) {
        byte[] encryptBytes = BaseEncoding.base32().decode( key );
        byte[] keyBytes = encryptor.decrypt( encryptBytes );

        boolean valid = TOTPValidator.window( 1 ).isValid( keyBytes,
                                                           TimeUnit.SECONDS.toMillis( 30 ), 6,
                                                           HmacShaAlgorithm.HMAC_SHA_1, token );

        if ( !valid ) {
            model.addAttribute( "keyString", key );
            model.addAttribute( "warning", "Provided token was invalid." );

            return "otp-new-key";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OTPUserDetailsImpl userDetails = (OTPUserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        user.setTotpKey( encryptBytes );
        userRepository.save( user );

        return "redirect:/otp/setup";
    }

    @PostMapping( "/otp/setup/disable" )
    public String disableKey( Model model ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OTPUserDetailsImpl userDetails = (OTPUserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        user.setTotpKey( null );
        userRepository.save( user );

        return "redirect:/otp/setup";
    }

    @RequestMapping( value = "/otp/setup/key-image", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE )
    public ResponseEntity<InputStreamResource> keyImage( String key ) throws IOException {
        byte[] encryptBytes = BaseEncoding.base32().decode( key );
        byte[] keyBytes = encryptor.decrypt( encryptBytes );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OTPUserDetailsImpl userDetails = (OTPUserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        String secretKey = BaseEncoding.base32().encode( keyBytes );
        OTPKey otpKey = new OTPKey( secretKey, OTPType.TOTP );
        String issuer = "SilVideo";
        String label = issuer + ":" + user.getUsername();

        // Create the OTP Auth URI.
        OTPAuthURI uri = OTPAuthURIBuilder.fromKey( otpKey ).label( label ).issuer( issuer )
                   .digits( 6 ).timeStep( 30000L ).build();

        // Render a QR Code into a file.
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        QRCodeWriter.fromURI( uri ).width( 300 ).height( 300 ).errorCorrectionLevel( ErrorCorrectionLevel.H )
                .margin( 4 ).imageFormatName( "PNG" ).write( bout );
        return ResponseEntity.ok( new InputStreamResource( new ByteArrayInputStream( bout.toByteArray() ) ) );
    }

    @PostMapping( "/otp/setup/test" )
    public String testOtp( Model model, String token ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OTPUserDetailsImpl userDetails = (OTPUserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        boolean valid = TOTPValidator.window( 1 ).isValid( encryptor.decrypt( user.getTotpKey() ),
                                                           TimeUnit.SECONDS.toMillis( 30 ), 6,
                                                           HmacShaAlgorithm.HMAC_SHA_1, token );

        model.addAttribute( "testResult", valid );

        return "otp-test";
    }

    @Autowired
    private OTPLoginSuccess loginSuccessHandler;

    @PostMapping( "/otp/auth/login" )
    public String loginOtp( Model model, String token, HttpServletRequest request,
                            HttpServletResponse response ) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();

        Authentication authentication = context.getAuthentication();
        OTPUserDetailsImpl userDetails = (OTPUserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        boolean valid = TOTPValidator.window( 1 ).isValid( encryptor.decrypt( user.getTotpKey() ),
                                                           TimeUnit.SECONDS.toMillis( 30 ), 6,
                                                           HmacShaAlgorithm.HMAC_SHA_1, token );

        if ( valid ) {
            if ( authentication instanceof PreOtpAuthentication ) {
                PreOtpAuthentication preAuth = ( (PreOtpAuthentication) authentication );
                preAuth.passToken();
                context.setAuthentication( preAuth );
            }

            loginSuccessHandler.onAuthenticationSuccess( request, response, authentication );

            return null;
        } else {
            model.addAttribute( "failed", true );
            return "otp-auth";
        }

    }

}
