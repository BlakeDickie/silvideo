/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.anidb.mylist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author bdickie
 */
@Component
public class ListReaderManager {

    @Autowired
    private ListReader reader;

    private ListImportState state;

    public ListReaderManager() {
        state = ListImportState.Error;
    }

    public synchronized ListImportState getState() {
        return state;
    }

}
