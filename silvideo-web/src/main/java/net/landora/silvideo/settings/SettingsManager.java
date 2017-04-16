/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 *
 * @author bdickie
 */
@Component
public class SettingsManager {

    @Autowired
    private SettingsRepository repo;

    private Settings currentSettings;

    public synchronized Settings getSettings() {
        if ( currentSettings == null ) {
            List<Settings> all = repo.findAll();
            if ( all.isEmpty() ) {
                currentSettings = new Settings();
            } else {
                currentSettings = all.get( 0 );
            }
            applyDefaults( currentSettings );
            currentSettings = repo.save( currentSettings );
        }
        return currentSettings;
    }

    public void saveSettings() {
        if ( currentSettings != null ) {
            currentSettings = repo.save( currentSettings );
        }
    }

    private void applyDefaults( Settings settings ) {
        if ( settings.getAnidbPassword() == null ) {
            settings.setAnidbPassword( "" );
        }
        if ( settings.getAnidbUsername() == null ) {
            settings.setAnidbUsername( "" );
        }
    }

}
