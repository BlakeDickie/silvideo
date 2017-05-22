/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.model;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 *
 * @author bdickie
 */
public class StorageFile {

    private String id;

    @DBRef
    private StorageRoot root;

    private String path;
    private String filename;

    private String ed2kHash;
    private String md5Sum;
    private String sha512Sum;

    private Long size;
    private DateTime lastModified;

    public StorageFile() {
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public StorageRoot getRoot() {
        return root;
    }

    public void setRoot( StorageRoot root ) {
        this.root = root;
    }

    public String getPath() {
        return path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename( String filename ) {
        this.filename = filename;
    }

    public String getEd2kHash() {
        return ed2kHash;
    }

    public void setEd2kHash( String ed2kHash ) {
        this.ed2kHash = ed2kHash;
    }

    public String getMd5Sum() {
        return md5Sum;
    }

    public void setMd5Sum( String md5Sum ) {
        this.md5Sum = md5Sum;
    }

    public String getSha512Sum() {
        return sha512Sum;
    }

    public void setSha512Sum( String sha512Sum ) {
        this.sha512Sum = sha512Sum;
    }

    public Long getSize() {
        return size;
    }

    public void setSize( Long size ) {
        this.size = size;
    }

    public DateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified( DateTime lastModified ) {
        this.lastModified = lastModified;
    }

}
