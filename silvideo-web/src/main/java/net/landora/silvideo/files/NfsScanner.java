/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files;

import net.landora.silvideo.files.model.NfsRootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import javax.annotation.PostConstruct;

/**
 *
 * @author bdickie
 */
@Component
public class NfsScanner {

    @Autowired
    private NfsRootRepository rootRepository;

    @PostConstruct
    public void init() throws IOException {
//        Nfs3 nfs = new Nfs3( "quon:/var/nfs-roots/storage/Videos", new CredentialUnix( 1000, 1000, Collections.EMPTY_SET ), 3 );
//        Nfs3File file = new Nfs3File( nfs, "/Muromi-san - 03.mkv" );
//        System.out.println( file.length() );
    }

}
