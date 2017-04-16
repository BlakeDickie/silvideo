/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author bdickie
 */
@Configuration
@EnableAsync
@EnableScheduling
public class ExecutorConfig {

    @Bean
    @Primary
    public ExecutorService executor() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public ScheduledExecutorService scheduledExecutor() {
        return Executors.newScheduledThreadPool( 3 );
    }

}
