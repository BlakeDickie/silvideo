/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.model;

import net.landora.silvideo.system.Computer;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 *
 * @author bdickie
 */
public class LocalRootConnection extends RootConnection {

    private String path;
    @DBRef
    private Computer computer;

    public LocalRootConnection() {
    }

    public String getPath() {
        return path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer( Computer computer ) {
        this.computer = computer;
    }

    @Override
    public String toPath() {
        return "file://" + getPath();
    }

    @Override
    public String toString() {
        return "LocalRootConnection{" + "path=" + path + '}';
    }

}
