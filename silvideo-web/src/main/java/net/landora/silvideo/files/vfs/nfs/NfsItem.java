/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.vfs.nfs;

import com.emc.ecs.nfsclient.nfs.io.Nfs3File;
import net.landora.silvideo.files.vfs.VfsItem;

/**
 *
 * @author bdickie
 */
public abstract class NfsItem implements VfsItem {

    protected Nfs3File file;

    public NfsItem( Nfs3File file ) {
        this.file = file;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String toString() {
        return getName();
    }
}
