package ru.mos.tender.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mos.tender.service.impl.NavURIBuilder;

@Configuration
public class Config {

    @Bean
    public NavURIBuilder navURIBuilder(){
        return new NavURIBuilder();
    }

}
