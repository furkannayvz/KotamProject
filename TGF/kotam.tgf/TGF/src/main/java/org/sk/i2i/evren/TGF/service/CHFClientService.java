package org.sk.i2i.evren.TGF.service;

import org.sk.i2i.evren.TGF.dto.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class CHFClientService {
    
    private final HttpClient httpClient;
    private final String chfBaseUrl;
    
    public CHFClientService(String chfBaseUrl) {
        this.chfBaseUrl = chfBaseUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }
    
    public void sendSmsCharging(SmsTransaction smsTransaction) {
        try {
            String jsonBody = String.format("""
                {
                    "senderMsisdn": "%s",
                    "receiverMsisdn": "%s",
                    "location": %d,
                }
                """, 
                smsTransaction.getSenderMsisdn(),
                smsTransaction.getReceiverMsisdn(),
                smsTransaction.getLocation(),
                LocalDateTime.now().toString()
            );
            
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(chfBaseUrl + "/charging/sms"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .timeout(Duration.ofSeconds(10))
                    .build();
            
            httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            System.out.println("SMS charged: " + smsTransaction.getSenderMsisdn());
                        } else {
                            System.err.println("SMS charge failed: " + response.statusCode());
                        }
                    });
                    
        } catch (Exception e) {
            System.err.println("Error sending SMS charge: " + e.getMessage());
        }
    }
    
    public void sendVoiceCharging(VoiceTransaction voiceTransaction) {
        try {
            String jsonBody = String.format("""
                {
                    "callerMsisdn": "%s",
                    "calleeMsisdn": "%s",
                    "location": %d,
                    "duration": %d,
                }
                """, 
                voiceTransaction.getCallerMsisdn(),
                voiceTransaction.getCalleeMsisdn(),
                voiceTransaction.getLocation(),
                voiceTransaction.getDuration(),
                LocalDateTime.now().toString()
            );
            
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(chfBaseUrl + "/charging/voice"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .timeout(Duration.ofSeconds(10))
                    .build();
            
            httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            System.out.println("Voice charged: " + voiceTransaction.getCallerMsisdn());
                        } else {
                            System.err.println("Voice charge failed: " + response.statusCode());
                        }
                    });
                    
        } catch (Exception e) {
            System.err.println("Error sending Voice charge: " + e.getMessage());
        }
    }
    
    public void sendDataCharging(DataTransaction dataTransaction) {
        try {
            String jsonBody = String.format("""
                {
                    "msisdn": "%s",
                    "location": %d,
                    "dataUsage": %d,
                    "ratingGroup": %d,
                }
                """, 
                dataTransaction.getMsisdn(),
                dataTransaction.getLocation(),
                dataTransaction.getDataUsage(),
                dataTransaction.getRatingGroup(),
                LocalDateTime.now().toString()
            );
            
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(chfBaseUrl + "/charging/data"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .timeout(Duration.ofSeconds(10))
                    .build();
            
            httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            System.out.println("Data charged: " + dataTransaction.getMsisdn());
                        } else {
                            System.err.println("Data charge failed: " + response.statusCode());
                        }
                    });
                    
        } catch (Exception e) {
            System.err.println("Error sending Data charge: " + e.getMessage());
        }
    }
}
