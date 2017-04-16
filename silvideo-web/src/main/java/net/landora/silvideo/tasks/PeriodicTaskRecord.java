/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.tasks;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;

/**
 *
 * @author bdickie
 */
@Document
public class PeriodicTaskRecord {

    private String id;

    @Indexed
    private String taskId;

    private DateTime lastRun;

    public PeriodicTaskRecord() {
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId( String taskId ) {
        this.taskId = taskId;
    }

    public DateTime getLastRun() {
        return lastRun;
    }

    public void setLastRun( DateTime lastRun ) {
        this.lastRun = lastRun;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode( this.taskId );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final PeriodicTaskRecord other = (PeriodicTaskRecord) obj;
        if ( !Objects.equals( this.taskId, other.taskId ) ) {
            return false;
        }
        return true;
    }

}
