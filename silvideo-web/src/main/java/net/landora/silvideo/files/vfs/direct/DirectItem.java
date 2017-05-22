/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.vfs.direct;

import net.landora.silvideo.files.vfs.VfsItem;
import java.io.File;

/**
 *
 * @author bdickie
 */
public abstract class DirectItem implements VfsItem {

    protected File file;

    public DirectItem( File file ) {
        this.file = file;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public String toString() {
        return getName();
    }

}
