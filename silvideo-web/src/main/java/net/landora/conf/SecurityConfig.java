/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.conf;

import net.landora.otp.OTPLoginSuccess;
import net.landora.otp.OtpAuthenticationProvider;
import net.landora.otp.impl.MongoUserSource;
import net.landora.otp.impl.User;
import net.landora.otp.impl.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.annotation.PostConstruct;

/**
 *
 * @author Blake Dickie
 */
@Configuration
@EnableGlobalMethodSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value( "${silvideo.security.encryption-password}" )
    private String encryptionPassword;

    @Value( "${silvideo.security.salt}" )
    private String encryptionSalt;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder( encryptionSalt );
    }

    @Bean
    public SecureRandom secureRandom() throws NoSuchAlgorithmException {
        return SecureRandom.getInstanceStrong();
    }

    @PostConstruct
    public void defaultUserSetup() {
        long users = userRepository.count();
        if ( users == 0L ) {
            User defaultUser = new User();
            defaultUser.setUsername( "user" );
            defaultUser.setHashedPassword( passwordEncoder().encode( "hawkpath" ) );
            userRepository.save( defaultUser );
        }
    }

    @Bean
    public BytesEncryptor encryptor() {
        return Encryptors.stronger( encryptionPassword, encryptionSalt );
    }

    @Bean
    public OTPLoginSuccess loginSuccessHandler() {
        OTPLoginSuccess loginHandler = new OTPLoginSuccess();
        return loginHandler;
    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http
                .authorizeRequests()
                .antMatchers( "/bootstrap/**" ).permitAll()
                .antMatchers( "/otp/auth/**" ).permitAll()
                .anyRequest().hasRole( "USER" )
                .and()
                .formLogin()
                .loginPage( "/login" )
                .successHandler( loginSuccessHandler() )
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
    
    @Bean
    public MongoUserSource mongoUserSource() {
        return new MongoUserSource();
    }

    public OtpAuthenticationProvider authenticationProvider() throws Exception {
        OtpAuthenticationProvider provider = new OtpAuthenticationProvider();
        provider.setPasswordEncoder( passwordEncoder() );
        provider.setUserDetailsService( mongoUserSource() );
        provider.setForcePrincipalAsString( false );
        return provider;
    }

    @Override
    protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
        auth.authenticationProvider( authenticationProvider() );
    }

}
