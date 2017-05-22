/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.vfs.direct;

import net.landora.silvideo.files.vfs.VfsFile;
import org.joda.time.DateTime;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author bdickie
 */
public class DirectFile extends DirectItem implements VfsFile {

    public DirectFile( File file ) {
        super( file );
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public DateTime getLastModified() {
        return new DateTime( file.lastModified() );
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return new BufferedInputStream( new FileInputStream( file ) );
    }

}
