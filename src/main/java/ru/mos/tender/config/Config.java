package ru.mos.tender.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mos.tender.service.impl.NavURIBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class Config {

    @Bean
    public NavURIBuilder navURIBuilder(){
        return new NavURIBuilder();
    }

    @Bean
    public ExecutorService widgetCreatingExecutorService() {
        return Executors.newFixedThreadPool(1);
    }
}
