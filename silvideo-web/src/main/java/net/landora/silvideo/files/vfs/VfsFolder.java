/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.vfs;

import java.util.List;

/**
 *
 * @author bdickie
 */
public interface VfsFolder extends VfsItem {

    public List<? extends VfsItem> getChildren();

}
