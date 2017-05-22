/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.vfs;

import org.joda.time.DateTime;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author bdickie
 */
public interface VfsFile extends VfsItem {

    public long getSize();

    public DateTime getLastModified();

    public InputStream openInputStream() throws IOException;

}
