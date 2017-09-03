/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files.vfs.nfs;

/**
 *
 * @author bdickie
 */
public class NfsException extends RuntimeException {

    public NfsException( String message, Throwable cause ) {
        super( message, cause );
    }

    public NfsException( Throwable cause ) {
        super( cause );
    }

}
