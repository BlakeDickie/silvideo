/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.tasks;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

/**
 *
 * @author bdickie
 */
@Component
public class PeriodicTaskManager {

    @Autowired
    private List<PeriodicTask> tasks;

    @Autowired
    private PeriodicTaskRecordRepository repository;

    @Autowired
    private ScheduledExecutorService scheduleExecutor;

    @Autowired
    private ExecutorService executor;

    private ScheduledFuture nextRunFuture;

    private final TreeMap<DateTime, PeriodicTask> nextRunMap = new TreeMap<>();

    public PeriodicTaskManager() {
    }

    @PostConstruct
    public void init() {
        for ( PeriodicTask trask : tasks ) {
            scheduleNextRun( trask );
        }

        calculateNextRunTime();
    }

    private synchronized void scheduleNextRun( PeriodicTask task ) {
        PeriodicTaskRecord record = repository.findByTaskId( task.getTaskId() );
        if ( record == null ) {
            record = new PeriodicTaskRecord();
            record.setTaskId( task.getTaskId() );
            record = repository.save( record );
        }

        DateTime lastRun = record.getLastRun();
        DateTime nextRun;
        if ( lastRun == null ) {
            nextRun = DateTime.now();
        } else {
            nextRun = lastRun.plus( task.getRunPeriod() );
        }
        nextRunMap.put( nextRun, task );
    }

    private synchronized void calculateNextRunTime() {

        if ( nextRunFuture != null ) {
            nextRunFuture.cancel( false );
        }

        if ( nextRunMap.isEmpty() ) {
            return;
        }

        DateTime nextRun = nextRunMap.firstKey();
        long delay = nextRun.getMillis() - System.currentTimeMillis();

        if ( delay < 1 ) {
            delay = 1;
        }

        nextRunFuture = scheduleExecutor.schedule(
        () -> {
            checkForReadyRuns();
        }, delay, TimeUnit.MILLISECONDS );
    }

    public synchronized void checkForReadyRuns() {

        SortedMap<DateTime, PeriodicTask> headMap = nextRunMap.headMap( DateTime.now().plusMillis( 1 ) );
        for ( Iterator<Map.Entry<DateTime, PeriodicTask>> i = headMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<DateTime, PeriodicTask> entry = i.next();
            i.remove();

            executor.submit( () -> {
                runTask( entry.getValue() );
            } );
        }

        calculateNextRunTime();
    }

    private void runTask( PeriodicTask task ) {
        task.runTask();

        PeriodicTaskRecord record = repository.findByTaskId( task.getTaskId() );
        record.setLastRun( DateTime.now() );
        repository.save( record );

        scheduleNextRun( task );
        calculateNextRunTime();
    }

}
