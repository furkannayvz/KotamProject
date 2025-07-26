package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class VoltDBConfig {

    @Value("${voltdb.host:34.142.55.240}")
    private String voltdbHost;

    @Value("${voltdb.api.port:8081}")
    private int voltdbApiPort;

    @Bean
    public WebClient voltdbWebClient() {
        return WebClient.builder()
                .baseUrl("http://" + voltdbHost + ":" + voltdbApiPort)
                .build();
    }

    public String getVoltdbHost() {
        return voltdbHost;
    }

    public int getVoltdbApiPort() {
        return voltdbApiPort;
    }

    public String getConnectionInfo() {
        return "VoltDB REST API: http://" + voltdbHost + ":" + voltdbApiPort;
    }
}
