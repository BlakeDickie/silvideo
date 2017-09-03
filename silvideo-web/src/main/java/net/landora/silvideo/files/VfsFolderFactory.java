/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files;

import com.emc.ecs.nfsclient.nfs.io.Nfs3File;
import com.emc.ecs.nfsclient.nfs.nfs3.Nfs3;
import com.emc.ecs.nfsclient.rpc.CredentialUnix;
import net.landora.silvideo.files.model.LocalRootConnection;
import net.landora.silvideo.files.model.NfsRootConnection;
import net.landora.silvideo.files.model.RootConnection;
import net.landora.silvideo.files.vfs.VfsFolder;
import net.landora.silvideo.files.vfs.direct.DirectFolder;
import net.landora.silvideo.files.vfs.nfs.NfsException;
import net.landora.silvideo.files.vfs.nfs.NfsFolder;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

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
        } else if ( connection instanceof NfsRootConnection ) {
            return toVfsFolder( (NfsRootConnection) connection );
        } else {
            throw new IllegalArgumentException( "Unsupported connection: " + connection.getClass() );
        }

    }

    public DirectFolder toVfsFolder( LocalRootConnection connection ) {
        DirectFolder folder = new DirectFolder( new File( connection.getPath() ) );
        return folder;
    }

    public NfsFolder toVfsFolder( NfsRootConnection connection ) {
        try {
            Nfs3 nfs = new Nfs3( connection.getNfsPath(), new CredentialUnix( connection.getUserId(), connection.getGroupId(), Collections.EMPTY_SET ), 3 );
            Nfs3File file = new Nfs3File( nfs, "/" );
            return new NfsFolder( file );
        } catch ( IOException ex ) {
            throw new NfsException( ex );
        }
    }

}
