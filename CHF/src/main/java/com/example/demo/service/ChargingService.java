package com.example.demo.service;

import com.example.demo.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ChargingService {

    @Autowired
    private VoltDBRestService voltDBRestService;

    @Autowired
    private KafkaService kafkaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final double NOTIFICATION_THRESHOLD_PERCENTAGE = 0.80; // 80%

    /**
     * Process SMS charging request
     */
    public Mono<ChargingResponse> processSmsCharging(SmsChargingRequest request) {
        String msisdn = request.getSenderMsisdn();
        return validateAndChargeSms(msisdn, request.getSmsCount())
                .doOnNext(response -> {
                    if (response.isSuccess()) {
                        logSmsTransaction(request);
                        checkAndSendBalanceNotification(msisdn, "SMS", response.getRemainingBalance(), response.getInitialBalance());
                    }
                })
                .onErrorReturn(createErrorResponse("SMS", msisdn, "System error occurred"));
    }

    /**
     * Process Voice charging request
     */
    public Mono<ChargingResponse> processVoiceCharging(VoiceChargingRequest request) {
        String msisdn = request.getCallerMsisdn();
        int minutes = (int) Math.ceil(request.getDuration() / 60.0);
        
        return validateAndChargeVoice(msisdn, minutes)
                .doOnNext(response -> {
                    if (response.isSuccess()) {
                        logVoiceTransaction(request, minutes);
                        checkAndSendBalanceNotification(msisdn, "VOICE", response.getRemainingBalance(), response.getInitialBalance());
                    }
                })
                .onErrorReturn(createErrorResponse("VOICE", msisdn, "System error occurred"));
    }

    /**
     * Process Data charging request
     */
    public Mono<ChargingResponse> processDataCharging(DataChargingRequest request) {
        String msisdn = request.getMsisdn();
        return validateAndChargeData(msisdn, request.getDataUsage())
                .doOnNext(response -> {
                    if (response.isSuccess()) {
                        logDataTransaction(request);
                        checkAndSendBalanceNotification(msisdn, "DATA", response.getRemainingBalance(), response.getInitialBalance());
                    }
                })
                .onErrorReturn(createErrorResponse("DATA", msisdn, "System error occurred"));
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
                        int packageId = balanceNode.get("PACKAGE_ID").asInt();
                        
                        return Mono.zip(
                            voltDBRestService.getPackageDetailsById(packageId), 
                            Mono.just(currentSmsBalance) 
                        )
                        .flatMap(tuple -> {
                            PackageDetailsDto packageDetails = tuple.getT1();
                            int currentBalance = tuple.getT2(); 
                            int initialSmsBalance = packageDetails.getAmountSms(); 
                            
                            if (currentBalance >= smsCount) {
                                int newBalance = currentBalance - smsCount;
                                return voltDBRestService.updateSmsBalance(msisdn, newBalance)
                                        .map(updateResult -> {
                                            ChargingResponse response = new ChargingResponse(true, 
                                                "SMS charged successfully", msisdn, "SMS");
                                            response.setRemainingBalance(newBalance);
                                            response.setInitialBalance(initialSmsBalance);
                                            return response;
                                        });
                            } else {
                                return Mono.just(new ChargingResponse(false, 
                                    "Insufficient SMS balance. Required: " + smsCount + ", Available: " + currentBalance, 
                                    msisdn, "SMS"));
                            }
                        });
                    } catch (Exception e) {
                        System.err.println("Error parsing balance or package ID for MSISDN " + msisdn + ": " + e.getMessage());
                        return Mono.error(new RuntimeException("Error processing SMS charging."));
                    }
                })
                .onErrorResume(RuntimeException.class, e -> 
                    Mono.just(createErrorResponse("SMS", msisdn, "System error during SMS charging: " + e.getMessage())));
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
                        int packageId = balanceNode.get("PACKAGE_ID").asInt(); 
                        
                        return Mono.zip(
                            voltDBRestService.getPackageDetailsById(packageId), 
                            Mono.just(currentMinutesBalance) 
                        )
                        .flatMap(tuple -> {
                            PackageDetailsDto packageDetails = tuple.getT1();
                            int currentBalance = tuple.getT2(); 
                            int initialMinutesBalance = packageDetails.getAmountMinutes(); 
                            
                            if (currentBalance >= minutes) {
                                int newBalance = currentBalance - minutes;
                                return voltDBRestService.updateMinutesBalance(msisdn, newBalance)
                                        .map(updateResult -> {
                                            ChargingResponse response = new ChargingResponse(true, 
                                                "Voice charged successfully", msisdn, "VOICE");
                                            response.setRemainingBalance(newBalance);
                                            response.setInitialBalance(initialMinutesBalance); 
                                            return response;
                                        });
                            } else {
                                return Mono.just(new ChargingResponse(false, 
                                    "Insufficient voice balance. Required: " + minutes + " minutes, Available: " + currentBalance, 
                                    msisdn, "VOICE"));
                            }
                        });
                    } catch (Exception e) {
                        System.err.println("Error parsing balance or package ID for MSISDN " + msisdn + ": " + e.getMessage());
                        return Mono.error(new RuntimeException("Error processing Voice charging."));
                    }
                })
                .onErrorResume(RuntimeException.class, e -> 
                    Mono.just(createErrorResponse("VOICE", msisdn, "System error during Voice charging: " + e.getMessage())));
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
                        int packageId = balanceNode.get("PACKAGE_ID").asInt(); 
                        
                        return Mono.zip(
                            voltDBRestService.getPackageDetailsById(packageId), 
                            Mono.just(currentDataBalance) 
                        )
                        .flatMap(tuple -> {
                            PackageDetailsDto packageDetails = tuple.getT1();
                            int currentBalance = tuple.getT2(); 
                            int initialDataBalance = packageDetails.getAmountData(); 
                            
                            if (currentBalance >= dataUsageMB) {
                                int newBalance = currentBalance - dataUsageMB;
                                return voltDBRestService.updateDataBalance(msisdn, newBalance)
                                        .map(updateResult -> {
                                            ChargingResponse response = new ChargingResponse(true, 
                                                "Data charged successfully", msisdn, "DATA");
                                            response.setRemainingBalance(newBalance);
                                            response.setInitialBalance(initialDataBalance); 
                                            return response;
                                        });
                            } else {
                                return Mono.just(new ChargingResponse(false, 
                                    "Insufficient data balance. Required: " + dataUsageMB + "MB, Available: " + currentBalance + "MB", 
                                    msisdn, "DATA"));
                            }
                        });
                    } catch (Exception e) {
                        System.err.println("Error parsing balance or package ID for MSISDN " + msisdn + ": " + e.getMessage());
                        return Mono.error(new RuntimeException("Error processing Data charging."));
                    }
                })
                .onErrorResume(RuntimeException.class, e -> 
                    Mono.just(createErrorResponse("DATA", msisdn, "System error during Data charging: " + e.getMessage())));
    }

    /**
     * Checks if the remaining balance is below the notification threshold and sends a Kafka notification.
     * This method now fetches customer details separately for the notification.
     * @param msisdn The MSISDN of the customer.
     * @param usageType The type of usage (SMS, VOICE, DATA).
     * @param remainingBalance The current remaining balance for the given usage type.
     * @param initialBalance The initial balance for the given usage type (from package).
     */
    private void checkAndSendBalanceNotification(String msisdn, String usageType, int remainingBalance, int initialBalance) {
        if (initialBalance <= 0) {
            System.out.println("Warning: Initial balance for " + msisdn + " (" + usageType + ") is zero or less. Cannot calculate percentage for notification.");
            return;
        }

        double percentageUsed = (double) (initialBalance - remainingBalance) / initialBalance;
        
        if (percentageUsed >= NOTIFICATION_THRESHOLD_PERCENTAGE) {
            System.out.println("Balance for MSISDN " + msisdn + " (" + usageType + ") is at " + 
                               String.format("%.2f", percentageUsed * 100) + "% usage. Attempting to send notification.");

            voltDBRestService.getCustomerDetailsForNotification(msisdn)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(notification -> {
                    notification.setMsisdn(msisdn); 
                    
                    try {
                        notification.setType(NotificationMessage.Type.valueOf(usageType));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid usage type for notification: " + usageType + ". Defaulting to SMS.");
                        notification.setType(NotificationMessage.Type.SMS);
                    }
                    
                    notification.setThreshold(String.format("%.0f%%", NOTIFICATION_THRESHOLD_PERCENTAGE * 100));
                    notification.setAmount(String.valueOf(initialBalance));
                    notification.setRemaining(String.valueOf(remainingBalance));

                    kafkaService.sendNotificationEvent(notification);
                }, error -> {
                    System.err.println("Error fetching customer details for MSISDN " + msisdn + ": " + error.getMessage());
                    NotificationMessage notification = new NotificationMessage();
                    notification.setMsisdn(msisdn);
                    notification.setThreshold(String.format("%.0f%%", NOTIFICATION_THRESHOLD_PERCENTAGE * 100));
                    notification.setAmount(String.valueOf(initialBalance));
                    notification.setRemaining(String.valueOf(remainingBalance));
                    try {
                        notification.setType(NotificationMessage.Type.valueOf(usageType));
                    } catch (IllegalArgumentException e) {
                        notification.setType(NotificationMessage.Type.SMS);
                    }
                    kafkaService.sendNotificationEvent(notification);
                });
        }
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
