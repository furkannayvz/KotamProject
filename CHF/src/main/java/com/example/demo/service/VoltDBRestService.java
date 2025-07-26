package com.example.demo.service;

import com.example.demo.dto.ChargingResult;
import com.example.demo.dto.UsageDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class VoltDBRestService {
    
    @Autowired
    private RestTemplate voltdbRestTemplate;
    
    @Autowired
    private String voltdbBaseUrl;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Update usage balance using specific VoltDB API endpoints
     */
    public ChargingResult updateUsageBalance(UsageDTO usage) {
        try {
            boolean updateSuccess = false;
            double cost = calculateCost(usage);
            
            // Update balance based on usage type using specific endpoints
            switch (usage.getUsageType().toUpperCase()) {
                case "INTERNET":
                case "DATA":
                    updateSuccess = updateDataBalance(usage.getMsisdn(), -usage.getAmount());
                    break;
                case "SMS":
                    updateSuccess = updateSmsBalance(usage.getMsisdn(), -usage.getAmount());
                    break;
                case "VOICE":
                case "MINUTES":
                    updateSuccess = updateMinutesBalance(usage.getMsisdn(), -usage.getAmount());
                    break;
            }
            
            if (updateSuccess) {
                // Get the updated balance
                int remainingBalance = getRemainingBalance(usage.getMsisdn(), usage.getUsageType());
                
                return new ChargingResult(
                    usage.getMsisdn(),
                    usage.getUsageType(),
                    usage.getAmount(),
                    cost,
                    remainingBalance,
                    true
                );
            }
            
            ChargingResult failureResult = new ChargingResult();
            failureResult.setSuccess(false);
            failureResult.setErrorMessage("Failed to update balance in VoltDB");
            return failureResult;
            
        } catch (Exception e) {
            ChargingResult errorResult = new ChargingResult();
            errorResult.setSuccess(false);
            errorResult.setErrorMessage("VoltDB API error: " + e.getMessage());
            return errorResult;
        }
    }
    
    /**
     * Update data balance using PUT /balances/data
     */
    public boolean updateDataBalance(String msisdn, int dataAmount) {
        try {
            // First get current balance
            int currentBalance = getCurrentDataBalance(msisdn);
            int newBalance = currentBalance + dataAmount; // dataAmount is negative for usage
            
            String url = "/balances/data?data=" + newBalance + "&msisdn=" + msisdn;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = voltdbRestTemplate.exchange(
                url, HttpMethod.PUT, request, String.class);
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Error updating data balance: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update SMS balance using PUT /balances/sms
     */
    public boolean updateSmsBalance(String msisdn, int smsAmount) {
        try {
            // First get current balance
            int currentBalance = getCurrentSmsBalance(msisdn);
            int newBalance = currentBalance + smsAmount; // smsAmount is negative for usage
            
            String url = "/balances/sms?sms=" + newBalance + "&msisdn=" + msisdn;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = voltdbRestTemplate.exchange(
                url, HttpMethod.PUT, request, String.class);
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Error updating SMS balance: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update minutes balance using PUT /balances/minutes
     */
    public boolean updateMinutesBalance(String msisdn, int minutesAmount) {
        try {
            // First get current balance
            int currentBalance = getCurrentMinutesBalance(msisdn);
            int newBalance = currentBalance + minutesAmount; // minutesAmount is negative for usage
            
            String url = "/balances/minutes?minutes=" + newBalance + "&msisdn=" + msisdn;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = voltdbRestTemplate.exchange(
                url, HttpMethod.PUT, request, String.class);
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Error updating minutes balance: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get remaining balance for a specific usage type
     */
    public int getRemainingBalance(String msisdn, String usageType) {
        try {
            BalanceInfo balance = getBalanceInfo(msisdn);
            if (balance != null) {
                switch (usageType.toUpperCase()) {
                    case "INTERNET":
                    case "DATA":
                        return balance.getBalLeftData();
                    case "SMS":
                        return balance.getBalLeftSms();
                    case "VOICE":
                    case "MINUTES":
                        return balance.getBalLeftMinutes();
                }
            }
            return 0;
        } catch (Exception e) {
            System.err.println("Error getting remaining balance: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Get complete balance information using GET /balances/{msisdn}
     */
    public BalanceInfo getBalanceInfo(String msisdn) {
        try {
            String url = "/balances/" + msisdn;
            
            ResponseEntity<String> response = voltdbRestTemplate.getForEntity(url, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                
                return new BalanceInfo(
                    jsonResponse.get("BALANCE_ID").asInt(),
                    jsonResponse.get("MSISDN").asText(),
                    jsonResponse.get("PACKAGE_ID").asInt(),
                    jsonResponse.get("BAL_LEFT_MINUTES").asInt(),
                    jsonResponse.get("BAL_LEFT_SMS").asInt(),
                    jsonResponse.get("BAL_LEFT_DATA").asInt()
                );
            }
        } catch (Exception e) {
            System.err.println("Error getting balance info: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get all balance information for a user
     */
    public Map<String, Integer> getAllBalances(String msisdn) {
        Map<String, Integer> balances = new HashMap<>();
        
        try {
            BalanceInfo balance = getBalanceInfo(msisdn);
            if (balance != null) {
                balances.put("DATA", balance.getBalLeftData());
                balances.put("SMS", balance.getBalLeftSms());
                balances.put("MINUTES", balance.getBalLeftMinutes());
            } else {
                balances.put("DATA", 0);
                balances.put("SMS", 0);
                balances.put("MINUTES", 0);
            }
        } catch (Exception e) {
            System.err.println("Error getting all balances: " + e.getMessage());
            balances.put("DATA", 0);
            balances.put("SMS", 0);
            balances.put("MINUTES", 0);
        }
        
        return balances;
    }
    
    /**
     * Get customer information using GET /customers/{msisdn}
     */
    public CustomerInfo getCustomerInfo(String msisdn) {
        try {
            String url = "/customers/" + msisdn;
            
            ResponseEntity<String> response = voltdbRestTemplate.getForEntity(url, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                
                return new CustomerInfo(
                    jsonResponse.get("msisdn").asText(),
                    jsonResponse.get("name").asText(),
                    jsonResponse.get("surname").asText(),
                    jsonResponse.get("email").asText(),
                    jsonResponse.get("national_id").asText()
                );
            }
        } catch (Exception e) {
            System.err.println("Error getting customer info: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get package information for a user using GET /packages/by-msisdn/{msisdn}
     */
    public PackageInfo getPackageInfo(String msisdn) {
        try {
            String url = "/packages/by-msisdn/" + msisdn;
            
            ResponseEntity<String> response = voltdbRestTemplate.getForEntity(url, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode jsonResponse = objectMapper.readTree(response.getBody());
                
                return new PackageInfo(
                    jsonResponse.get("PACKAGE_ID").asInt(),
                    jsonResponse.get("PACKAGE_NAME").asText(),
                    jsonResponse.get("PRICE").asDouble(),
                    jsonResponse.get("AMOUNT_MINUTES").asInt(),
                    jsonResponse.get("AMOUNT_DATA").asInt(),
                    jsonResponse.get("AMOUNT_SMS").asInt(),
                    jsonResponse.get("PERIOD").asInt()
                );
            }
        } catch (Exception e) {
            System.err.println("Error getting package info: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Test the VoltDB connection by getting max balance ID
     */
    public boolean testConnection() {
        try {
            String url = "/balances/max-Id";
            ResponseEntity<String> response = voltdbRestTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("VoltDB connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    // Helper methods to get current balances
    private int getCurrentDataBalance(String msisdn) {
        BalanceInfo balance = getBalanceInfo(msisdn);
        return balance != null ? balance.getBalLeftData() : 0;
    }
    
    private int getCurrentSmsBalance(String msisdn) {
        BalanceInfo balance = getBalanceInfo(msisdn);
        return balance != null ? balance.getBalLeftSms() : 0;
    }
    
    private int getCurrentMinutesBalance(String msisdn) {
        BalanceInfo balance = getBalanceInfo(msisdn);
        return balance != null ? balance.getBalLeftMinutes() : 0;
    }
    
    private double calculateCost(UsageDTO usage) {
        // Get package info to calculate accurate cost
        PackageInfo packageInfo = getPackageInfo(usage.getMsisdn());
        if (packageInfo != null) {
            double basePrice = packageInfo.getPrice();
            // Calculate cost based on package pricing
            switch (usage.getUsageType().toUpperCase()) {
                case "INTERNET":
                case "DATA":
                    return (usage.getAmount() / (double) packageInfo.getAmountData()) * basePrice;
                case "SMS":
                    return (usage.getAmount() / (double) packageInfo.getAmountSms()) * basePrice;
                case "VOICE":
                case "MINUTES":
                    return (usage.getAmount() / (double) packageInfo.getAmountMinutes()) * basePrice;
            }
        }
        
        // Fallback to simplified calculation
        switch (usage.getUsageType().toUpperCase()) {
            case "INTERNET":
            case "DATA":
                return usage.getAmount() * 0.01; // 0.01 per MB
            case "SMS":
                return usage.getAmount() * 0.05; // 0.05 per SMS
            case "VOICE":
            case "MINUTES":
                return usage.getAmount() * 0.10; // 0.10 per minute
            default:
                return 0.0;
        }
    }
    
    // Inner classes for data transfer
    public static class BalanceInfo {
        private int balanceId;
        private String msisdn;
        private int packageId;
        private int balLeftMinutes;
        private int balLeftSms;
        private int balLeftData;
        
        public BalanceInfo(int balanceId, String msisdn, int packageId, 
                          int balLeftMinutes, int balLeftSms, int balLeftData) {
            this.balanceId = balanceId;
            this.msisdn = msisdn;
            this.packageId = packageId;
            this.balLeftMinutes = balLeftMinutes;
            this.balLeftSms = balLeftSms;
            this.balLeftData = balLeftData;
        }
        
        // Getters
        public int getBalanceId() { return balanceId; }
        public String getMsisdn() { return msisdn; }
        public int getPackageId() { return packageId; }
        public int getBalLeftMinutes() { return balLeftMinutes; }
        public int getBalLeftSms() { return balLeftSms; }
        public int getBalLeftData() { return balLeftData; }
    }
    
    public static class CustomerInfo {
        private String msisdn;
        private String name;
        private String surname;
        private String email;
        private String nationalId;
        
        public CustomerInfo(String msisdn, String name, String surname, String email, String nationalId) {
            this.msisdn = msisdn;
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.nationalId = nationalId;
        }
        
        // Getters
        public String getMsisdn() { return msisdn; }
        public String getName() { return name; }
        public String getSurname() { return surname; }
        public String getEmail() { return email; }
        public String getNationalId() { return nationalId; }
    }
    
    public static class PackageInfo {
        private int packageId;
        private String packageName;
        private double price;
        private int amountMinutes;
        private int amountData;
        private int amountSms;
        private int period;
        
        public PackageInfo(int packageId, String packageName, double price, 
                          int amountMinutes, int amountData, int amountSms, int period) {
            this.packageId = packageId;
            this.packageName = packageName;
            this.price = price;
            this.amountMinutes = amountMinutes;
            this.amountData = amountData;
            this.amountSms = amountSms;
            this.period = period;
        }
        
        // Getters
        public int getPackageId() { return packageId; }
        public String getPackageName() { return packageName; }
        public double getPrice() { return price; }
        public int getAmountMinutes() { return amountMinutes; }
        public int getAmountData() { return amountData; }
        public int getAmountSms() { return amountSms; }
        public int getPeriod() { return period; }
    }
}
