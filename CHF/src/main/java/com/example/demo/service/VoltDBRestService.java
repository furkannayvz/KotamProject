package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;

@Service
public class VoltDBRestService {

    @Autowired
    private WebClient voltdbWebClient;

    @Value("${voltdb.host:34.142.55.240}")
    private String voltdbHost;

    @Value("${voltdb.api.port:8081}")
    private int voltdbApiPort;

    private final ObjectMapper objectMapper;

    public VoltDBRestService() {
        this.objectMapper = new ObjectMapper();
    }

    public boolean testConnection() {
        try {
            String result = voltdbWebClient
                    .get()
                    .uri("/customers/max-msisdn")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();
            
            return result != null && !result.trim().isEmpty();
        } catch (Exception e) {
            System.err.println("VoltDB connection test failed: " + e.getMessage());
            return false;
        }
    }

    public Mono<String> getCustomerByMsisdn(String msisdn) {
        return voltdbWebClient
                .get()
                .uri("/customers/{msisdn}", msisdn)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<String> getCustomerEmail(String msisdn) {
        return voltdbWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/customers/email")
                    .queryParam("msisdn", msisdn)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<String> getCustomerPassword(String msisdn) {
        return voltdbWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/customers/password")
                    .queryParam("msisdn", msisdn)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<String> getBalanceByMsisdn(String msisdn) {
        return voltdbWebClient
                .get()
                .uri("/balances/{msisdn}", msisdn)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<String> getPackageByMsisdn(String msisdn) {
        return voltdbWebClient
                .get()
                .uri("/packages/by-msisdn/{msisdn}", msisdn)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<String> getPackageNameByMsisdn(String msisdn) {
        return voltdbWebClient
                .get()
                .uri("/packages/name/by-msisdn/{msisdn}", msisdn)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<String> updateSmsBalance(String msisdn, int sms) {
        return voltdbWebClient
                .put()
                .uri(uriBuilder -> uriBuilder
                    .path("/balances/sms")
                    .queryParam("msisdn", msisdn)
                    .queryParam("sms", sms)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<String> updateMinutesBalance(String msisdn, int minutes) {
        return voltdbWebClient
                .put()
                .uri(uriBuilder -> uriBuilder
                    .path("/balances/minutes")
                    .queryParam("msisdn", msisdn)
                    .queryParam("minutes", minutes)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<String> updateDataBalance(String msisdn, int data) {
        return voltdbWebClient
                .put()
                .uri(uriBuilder -> uriBuilder
                    .path("/balances/data")
                    .queryParam("msisdn", msisdn)
                    .queryParam("data", data)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<String> checkCustomerExists(String email, String nationalId) {
        return voltdbWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("/customers/exists")
                    .queryParam("email", email)
                    .queryParam("national_id", nationalId)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public String getConnectionInfo() {
        return "VoltDB REST API: http://" + voltdbHost + ":" + voltdbApiPort;
    }

    public Mono<String> getMaxMsisdn() {
        return voltdbWebClient
                .get()
                .uri("/customers/max-msisdn")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }

    public Mono<String> getMaxBalanceId() {
        return voltdbWebClient
                .get()
                .uri("/balances/max-Id")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10));
    }
}
