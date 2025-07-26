package com.example.demo.service;

import com.example.demo.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChargingService {

    @Autowired
    private VoltDBRestService voltDBRestService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Process SMS charging request
     */
    public Mono<ChargingResponse> processSmsCharging(SmsChargingRequest request) {
        return validateAndChargeSms(request.getSenderMsisdn(), request.getSmsCount())
                .map(response -> {
                    if (response.isSuccess()) {
                        logSmsTransaction(request);
                    }
                    return response;
                })
                .onErrorReturn(createErrorResponse("SMS", request.getSenderMsisdn(), "System error occurred"));
    }

    /**
     * Process Voice charging request
     */
    public Mono<ChargingResponse> processVoiceCharging(VoiceChargingRequest request) {
        int minutes = (int) Math.ceil(request.getDuration() / 60.0);
        
        return validateAndChargeVoice(request.getCallerMsisdn(), minutes)
                .map(response -> {
                    if (response.isSuccess()) {
                        logVoiceTransaction(request, minutes);
                    }
                    return response;
                })
                .onErrorReturn(createErrorResponse("VOICE", request.getCallerMsisdn(), "System error occurred"));
    }

    /**
     * Process Data charging request
     */
    public Mono<ChargingResponse> processDataCharging(DataChargingRequest request) {
        return validateAndChargeData(request.getMsisdn(), request.getDataUsage())
                .map(response -> {
                    if (response.isSuccess()) {
                        logDataTransaction(request);
                    }
                    return response;
                })
                .onErrorReturn(createErrorResponse("DATA", request.getMsisdn(), "System error occurred"));
    }

    /**
     * Validate SMS balance and charge
     */
    private Mono<ChargingResponse> validateAndChargeSms(String msisdn, int smsCount) {
        return voltDBRestService.getBalanceByMsisdn(msisdn)
                .flatMap(balanceJson -> {
                    try {
                        JsonNode balanceNode = objectMapper.readTree(balanceJson);
                        int currentSmsBalance = balanceNode.get("BAL_LEFT_SMS").asInt();
                        
                        if (currentSmsBalance >= smsCount) {
                            int newBalance = currentSmsBalance - smsCount;
                            return voltDBRestService.updateSmsBalance(msisdn, newBalance)
                                    .map(updateResult -> {
                                        ChargingResponse response = new ChargingResponse(true, 
                                            "SMS charged successfully", msisdn, "SMS");
                                        response.setRemainingBalance(newBalance);
                                        return response;
                                    });
                        } else {
                            return Mono.just(new ChargingResponse(false, 
                                "Insufficient SMS balance. Required: " + smsCount + ", Available: " + currentSmsBalance, 
                                msisdn, "SMS"));
                        }
                    } catch (Exception e) {
                        return Mono.just(createErrorResponse("SMS", msisdn, "Error parsing balance: " + e.getMessage()));
                    }
                });
    }

    /**
     * Validate Voice balance and charge
     */
    private Mono<ChargingResponse> validateAndChargeVoice(String msisdn, int minutes) {
        return voltDBRestService.getBalanceByMsisdn(msisdn)
                .flatMap(balanceJson -> {
                    try {
                        JsonNode balanceNode = objectMapper.readTree(balanceJson);
                        int currentMinutesBalance = balanceNode.get("BAL_LEFT_MINUTES").asInt();
                        
                        if (currentMinutesBalance >= minutes) {
                            int newBalance = currentMinutesBalance - minutes;
                            return voltDBRestService.updateMinutesBalance(msisdn, newBalance)
                                    .map(updateResult -> {
                                        ChargingResponse response = new ChargingResponse(true, 
                                            "Voice charged successfully", msisdn, "VOICE");
                                        response.setRemainingBalance(newBalance);
                                        return response;
                                    });
                        } else {
                            return Mono.just(new ChargingResponse(false, 
                                "insufficient voice balance. Required: " + minutes + " minutes, Available: " + currentMinutesBalance, 
                                msisdn, "VOICE"));
                        }
                    } catch (Exception e) {
                        return Mono.just(createErrorResponse("VOICE", msisdn, "Error parsing balance: " + e.getMessage()));
                    }
                });
    }

    /**
     * Validate Data balance and charge
     */
    private Mono<ChargingResponse> validateAndChargeData(String msisdn, int dataUsageMB) {
        return voltDBRestService.getBalanceByMsisdn(msisdn)
                .flatMap(balanceJson -> {
                    try {
                        JsonNode balanceNode = objectMapper.readTree(balanceJson);
                        int currentDataBalance = balanceNode.get("BAL_LEFT_DATA").asInt();
                        
                        if (currentDataBalance >= dataUsageMB) {
                            int newBalance = currentDataBalance - dataUsageMB;
                            return voltDBRestService.updateDataBalance(msisdn, newBalance)
                                    .map(updateResult -> {
                                        ChargingResponse response = new ChargingResponse(true, 
                                            "Data charged successfully", msisdn, "DATA");
                                        response.setRemainingBalance(newBalance);
                                        return response;
                                    });
                        } else {
                            return Mono.just(new ChargingResponse(false, 
                                "Insufficient data balance. Required: " + dataUsageMB + "MB, Available: " + currentDataBalance + "MB", 
                                msisdn, "DATA"));
                        }
                    } catch (Exception e) {
                        return Mono.just(createErrorResponse("DATA", msisdn, "Error parsing balance: " + e.getMessage()));
                    }
                });
    }

    private ChargingResponse createErrorResponse(String type, String msisdn, String message) {
        return new ChargingResponse(false, message, msisdn, type);
    }

    private void logSmsTransaction(SmsChargingRequest request) {
        System.out.println("SMS Transaction Logged: " + request.getSenderMsisdn() + 
                          " -> " + request.getReceiverMsisdn() + 
                          " | Location: " + request.getLocation() + 
                          " | Count: " + request.getSmsCount());
    }

    private void logVoiceTransaction(VoiceChargingRequest request, int chargedMinutes) {
        System.out.println("Voice Transaction Logged: " + request.getCallerMsisdn() + 
                          " -> " + request.getCalleeMsisdn() + 
                          " | Location: " + request.getLocation() + 
                          " | Duration: " + request.getDuration() + "s" +
                          " | Charged: " + chargedMinutes + " minutes");
    }

    private void logDataTransaction(DataChargingRequest request) {
        System.out.println("Data Transaction Logged: " + request.getMsisdn() + 
                          " | Location: " + request.getLocation() + 
                          " | Usage: " + request.getDataUsage() + "MB" +
                          " | Rating Group: " + request.getRatingGroup());
    }
}