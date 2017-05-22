/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files;

import net.landora.silvideo.files.model.LocalRootConnection;
import net.landora.silvideo.files.model.RootConnection;
import net.landora.silvideo.files.vfs.VfsFolder;
import net.landora.silvideo.files.vfs.direct.DirectFolder;
import org.springframework.stereotype.Component;
import java.io.File;

/**
 *
 * @author bdickie
 */
@Component
public class VfsFolderFactory {

    public VfsFolder toVfsFolder( RootConnection connection ) {

        if ( connection == null ) {
            return null;
        }

        if ( connection instanceof LocalRootConnection ) {
            return toVfsFolder( (LocalRootConnection) connection );
        } else {
            throw new IllegalArgumentException( "Unsupported connection: " + connection.getClass() );
        }

    }

    public DirectFolder toVfsFolder( LocalRootConnection connection ) {
        DirectFolder folder = new DirectFolder( new File( connection.getPath() ) );
        return folder;
    }

}
