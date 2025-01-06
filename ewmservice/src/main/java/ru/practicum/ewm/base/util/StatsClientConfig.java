package ru.practicum.ewm.base.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.StatsClient;
import ru.practicum.StatsClient2;

@Configuration
public class StatsClientConfig {

    @Value("${stats.url}")
    private String urlOfStat;

    @Bean
    StatsClient statsClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new StatsClient2(urlOfStat, builder);
    }
}