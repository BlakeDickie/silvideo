/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.anidb.tasks;

import net.landora.silvideo.anidb.AniDBInfo;
import net.landora.silvideo.tasks.PeriodicTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author bdickie
 */
@Component
public class NotificationsTask implements PeriodicTask {

    @Autowired
    private AniDBInfo info;

    @Override
    public String getTaskId() {
        return NotificationsTask.class.getName();
    }

    @Override
    public long getRunPeriod() {
        return TimeUnit.HOURS.toMillis( 1L );
    }

    @Override
    public void runTask() {
        info.checkNotifications();
    }

}
