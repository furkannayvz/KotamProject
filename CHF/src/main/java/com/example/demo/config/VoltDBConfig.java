package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.time.Duration;

@Configuration
public class VoltDBConfig {
    
    @Value("${voltdb.api.host:34.142.55.240}")
    private String voltdbHost;
    
    @Value("${voltdb.api.port:8081}")
    private int voltdbPort;
    
    @Value("${voltdb.api.timeout:30}")
    private int timeoutSeconds;
    
    @Value("${voltdb.api.base-url:}")
    private String baseUrl;
    
    @Bean
    public RestTemplate voltdbRestTemplate(RestTemplateBuilder builder) {
        if (baseUrl.isEmpty()) {
            baseUrl = "http://" + voltdbHost + ":" + voltdbPort;
        }
        
        System.out.println("configuring VoltDB REST client for: " + baseUrl);
        
        return builder
                .rootUri(baseUrl)
                .setConnectTimeout(Duration.ofSeconds(timeoutSeconds))
                .setReadTimeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }
    
    @Bean
    public String voltdbBaseUrl() {
        if (baseUrl.isEmpty()) {
            baseUrl = "http://" + voltdbHost + ":" + voltdbPort;
        }
        return baseUrl;
    }
}

