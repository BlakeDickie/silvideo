/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.vfs.direct;

import net.landora.silvideo.files.vfs.VfsFolder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bdickie
 */
public class DirectFolder extends DirectItem implements VfsFolder {

    public DirectFolder( File file ) {
        super( file );
    }

    @Override
    public List<DirectItem> getChildren() {
        List<DirectItem> files = new ArrayList<>();

        for ( File child : file.listFiles() ) {
            if ( child.isDirectory() ) {
                files.add( new DirectFolder( child ) );
            } else if ( child.isFile() ) {
                files.add( new DirectFile( child ) );
            }
        }
        return files;
    }

}
