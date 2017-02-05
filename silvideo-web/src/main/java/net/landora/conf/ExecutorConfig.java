/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 *
 * @author bdickie
 */
@Configuration
@EnableAsync
public class ExecutorConfig {

    @Bean
    public ExecutorService executor() {
        return Executors.newCachedThreadPool();
    }

}
