package net.landora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.context.support.StandardServletEnvironment;

@SpringBootApplication
public class SilvideoWebApplication {

    public static void main( String[] args ) {
        SpringApplication app = new SpringApplication( SilvideoWebApplication.class );
        StandardServletEnvironment env = new StandardServletEnvironment();
        env.setDefaultProfiles( "develop" );
        app.setEnvironment( env );
        app.run( args );
    }
}
