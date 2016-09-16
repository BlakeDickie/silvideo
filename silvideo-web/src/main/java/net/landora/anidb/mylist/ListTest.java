/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.anidb.mylist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Blake Dickie
 */
@Component
public class ListTest implements CommandLineRunner {

    @Autowired
    private ListReader reader;

    @Override
    public void run( String... args ) throws Exception {
        try ( InputStream is = new BufferedInputStream( new FileInputStream( "/home/qhrdev/code/1473657857-8654-76916.tgz" ) ) ) {
            reader.download( is );
        } catch ( Throwable ex ) {
            Logger.getLogger( ListTest.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

}
