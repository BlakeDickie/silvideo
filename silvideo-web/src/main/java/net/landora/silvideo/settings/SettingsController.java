/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author bdickie
 */
@Controller
public class SettingsController {

    @Autowired
    private SettingsManager settingsManager;

    @RequestMapping( path = "/settings", method = RequestMethod.GET )
    public String showSettings( Model model ) {

        model.addAttribute( "settings", settingsManager.getSettings() );

        return "settings";

    }

    @RequestMapping( path = "/settings", method = RequestMethod.POST )
    public String saveSettings( Model model, String anidbUsername, String anidbPassword ) {

        Settings settings = settingsManager.getSettings();
        settings.setAnidbUsername( anidbUsername );
        if ( anidbPassword != null && !anidbPassword.isEmpty() ) {
            settings.setAnidbPassword( anidbPassword );
        }
        settingsManager.saveSettings();

        return showSettings( model );

    }
}
