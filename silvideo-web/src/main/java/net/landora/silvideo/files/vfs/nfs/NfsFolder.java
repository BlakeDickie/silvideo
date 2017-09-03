/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.vfs.nfs;

import com.emc.ecs.nfsclient.nfs.io.Nfs3File;
import net.landora.silvideo.files.vfs.VfsFolder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bdickie
 */
public class NfsFolder extends NfsItem implements VfsFolder {

    public NfsFolder( Nfs3File file ) {
        super( file );
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public List<? extends NfsItem> getChildren() {
        try {
            List<Nfs3File> listFiles = file.listFiles();
            List<NfsItem> children = new ArrayList<>( listFiles.size() );
            for ( Nfs3File child : listFiles ) {
                if ( child.isDirectory() ) {
                    children.add( new NfsFolder( child ) );
                } else {
                    children.add( new NfsFile( child ) );
                }
            }
            return children;
        } catch ( IOException ex ) {
            throw new NfsException( ex );
        }
    }

}
