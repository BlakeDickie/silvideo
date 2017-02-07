/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.conf;

import net.landora.security.UserDetailsCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author Blake Dickie
 */
@Configuration
@EnableGlobalMethodSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private WebInvocationPrivilegeEvaluator evaluator;

    @Bean
    public SecureRandom secureRandom() throws NoSuchAlgorithmException {
        return SecureRandom.getInstanceStrong();
    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http
                .authorizeRequests()
                .antMatchers( "/bootstrap/**", "/", "/login" ).permitAll()
                .anyRequest().hasRole( "APPROVED" )
                .and()
                .logout()
                .logoutSuccessUrl( "/" )
                .permitAll()
                .and()
                .addFilterBefore( ssoFilter(), BasicAuthenticationFilter.class );;
    }

    private OAuth2ClientAuthenticationProcessingFilter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter( "/login" );
        OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate( google(), oauth2ClientContext );
        facebookFilter.setRestTemplate( facebookTemplate );
        UserInfoTokenServices tokenServices = new UserInfoTokenServices( googleResource().getUserInfoUri(), google().getClientId() );
        tokenServices.setRestTemplate( facebookTemplate );
        tokenServices.setAuthoritiesExtractor( userDetailsCustomizer() );
        facebookFilter.setTokenServices( tokenServices );
        return facebookFilter;
    }

    @Bean
    @ConfigurationProperties( "google.client" )
    public AuthorizationCodeResourceDetails google() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties( "google.resource" )
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter ) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter( filter );
        registration.setOrder( -100 );
        return registration;
    }

    @Bean
    public UserDetailsCustomizer userDetailsCustomizer() {
        return new UserDetailsCustomizer();
    }

}
