/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.mylist;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author bdickie
 */
@Component
public class ListReaderManager {

    @Autowired
    private Executor executor;

    @Autowired
    private ListReader reader;

    private ListImportState state;

    public ListReaderManager() {
        state = ListImportState.None;
    }

    public synchronized ListImportState getState() {
        return state;
    }

    private synchronized void setState( ListImportState state ) {
        this.state = state;
    }

    public synchronized boolean queueImport( byte[] listData ) {
        if ( state == ListImportState.Running ) {
            return false;
        }
        state = ListImportState.Running;

        executor.execute( () -> {
            startImportImpl( listData );
        } );

        return true;
    }

    private void startImportImpl( byte[] listData ) {
        try {
            reader.download( new ByteArrayInputStream( listData ) );
            setState( ListImportState.Completed );
        } catch ( Throwable ex ) {
            Logger.getLogger( ListReaderManager.class.getName() ).log( Level.SEVERE, null, ex );
            setState( ListImportState.Error );
        }
    }

}
