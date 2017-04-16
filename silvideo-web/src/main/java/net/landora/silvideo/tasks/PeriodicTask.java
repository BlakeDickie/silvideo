/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.tasks;

/**
 *
 * @author bdickie
 */
public interface PeriodicTask {

    public String getTaskId();

    public long getRunPeriod();

    public void runTask();

}
