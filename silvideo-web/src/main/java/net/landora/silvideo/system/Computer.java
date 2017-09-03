/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.system;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author bdickie
 */
@Document
public class Computer {

    private String id;
    private String systemName;

    public Computer() {
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName( String systemName ) {
        this.systemName = systemName;
    }

}
