/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.mylist;

import net.landora.silvideo.anidb.api.AniDBAPI;
import net.landora.silvideo.anidb.model.AnimeMessage;
import net.landora.silvideo.anidb.model.repo.AnimeMessageRepository;
import net.landora.silvideo.settings.Settings;
import net.landora.silvideo.settings.SettingsManager;
import net.landora.silvideo.tasks.PeriodicTask;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author bdickie
 */
@Component
public class MyListExportQueuer implements PeriodicTask {

    private Logger log = LoggerFactory.getLogger( getClass() );

    @Autowired
    private SettingsManager settingsManager;

    @Autowired
    private AnimeMessageRepository messageRepository;

    @Autowired
    private AniDBAPI api;

    @Autowired
    private ListReader listReader;

    private Pattern MYLIST_PATTERN = Pattern.compile(
                    "Direct Download: \\[url=(http://export.anidb.net/export/[0-9-]+.tgz)\\]",
                    Pattern.CASE_INSENSITIVE );

    @Override
    public String getTaskId() {
        return MyListExportQueuer.class.getName();
    }

    @Override
    public long getRunPeriod() {
        return TimeUnit.MINUTES.toMillis( 1l );
    }

    @Override
    public void runTask() {
        checkForUnproccessedMessages();

        Settings settings = settingsManager.getSettings();
        DateTime lastRequest = settings.getOutstandingAnidbExport();
        DateTime lastComplete = settings.getLastCompletedExport();

        if ( lastRequest != null ) {
            return;
        }

        // Wait a week until the next export.
        if ( lastComplete != null && lastComplete.isAfter( DateTime.now().minusWeeks( 1 ) ) ) {
            return;
        }

        api.queueMyListExport( "xml-plain-new" );
        DateTime now = DateTime.now();
        settings.setOutstandingAnidbExport( now );
        settingsManager.saveSettings();
    }

    private void checkForUnproccessedMessages() {
        List<AnimeMessage> messages = messageRepository.findByRemovedDateNull();
        for ( AnimeMessage message : messages ) {
            if ( message.getTitle().equalsIgnoreCase( "[EXPORT] Mylist Export is ready for download" ) ) {
                processMylistExport( message );
            }
        }
    }

    private void processMylistExport( AnimeMessage msg ) {
        Matcher m = MYLIST_PATTERN.matcher( msg.getBody() );
        if ( m.find() ) {
            try {
                String url = m.group( 1 );
                log.warn( "Starting MyList Download" );
                if ( listReader.download( new URL( url ) ) ) {
                    msg.setRemovedDate( DateTime.now() );
                    msg = messageRepository.save( msg );

                    Settings settings = settingsManager.getSettings();

                    DateTime lastQueued = settings.getOutstandingAnidbExport();
                    if ( lastQueued != null && lastQueued.isBefore( msg.getDate() ) ) {
                        settings.setLastCompletedExport( msg.getDate() );
                        settings.setOutstandingAnidbExport( null );
                        settingsManager.saveSettings();
                    }

                }
                log.warn( "Finished MyList Download" );
            } catch ( Throwable ex ) {
                LoggerFactory.getLogger( getClass() ).error( "Error processing MyList export.", ex );
            }
        }
    }

}
