/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import java.util.Arrays;
import java.util.Properties;
import javax.servlet.ServletContext;

/**
 *
 * @author Blake Dickie
 */
@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers( ViewControllerRegistry registry ) {
        registry.addViewController( "/home" ).setViewName( "welcome" );
        registry.addViewController( "/" ).setViewName( "welcome" );
        registry.addViewController( "/login" ).setViewName( "login" );
    }

    @Autowired
    protected FreeMarkerProperties properties;

    protected void applyProperties( FreeMarkerConfigurationFactory factory ) {
        factory.setTemplateLoaderPaths( this.properties.getTemplateLoaderPath() );
        factory.setPreferFileSystemAccess( this.properties.isPreferFileSystemAccess() );
        factory.setDefaultEncoding( this.properties.getCharsetName() );
        Properties settings = new Properties();
        settings.putAll( this.properties.getSettings() );
        factory.setFreemarkerSettings( settings );
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer() {
                         @Override
                         public void setServletContext( ServletContext servletContext ) {
                             super.setServletContext( servletContext ); //To change body of generated methods, choose Tools | Templates.
                             getTaglibFactory().setClasspathTlds( Arrays.asList( "/META-INF/security.tld" ) );
                         }

                     };
        applyProperties( configurer );
        return configurer;
    }
}
