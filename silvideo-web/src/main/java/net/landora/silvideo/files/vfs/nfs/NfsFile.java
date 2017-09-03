/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.vfs.nfs;

import com.emc.ecs.nfsclient.nfs.io.Nfs3File;
import com.emc.ecs.nfsclient.nfs.io.NfsFileInputStream;
import net.landora.silvideo.files.vfs.VfsFile;
import org.joda.time.DateTime;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author bdickie
 */
public class NfsFile extends NfsItem implements VfsFile {

    public NfsFile( Nfs3File file ) {
        super( file );
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public long getSize() {
        try {
            return file.lengthEx();
        } catch ( IOException ex ) {
            throw new NfsException( ex );
        }
    }

    @Override
    public DateTime getLastModified() {
        try {
            return new DateTime( file.lastModified() );
        } catch ( IOException ex ) {
            throw new NfsException( ex );
        }
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return new BufferedInputStream( new NfsFileInputStream( file ) );
    }

}
