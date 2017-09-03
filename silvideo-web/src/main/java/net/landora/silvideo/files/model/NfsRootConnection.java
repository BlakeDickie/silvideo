/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.model;

/**
 *
 * @author bdickie
 */
public class NfsRootConnection extends RootConnection {

    private String nfsPath;
    private int userId;
    private int groupId;

    public NfsRootConnection() {
    }

    public String getNfsPath() {
        return nfsPath;
    }

    public void setNfsPath( String nfsPath ) {
        this.nfsPath = nfsPath;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId( int userId ) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId( int groupId ) {
        this.groupId = groupId;
    }

    @Override
    public String toPath() {
        return String.format( "nfs://%s", getNfsPath() );
    }

}
