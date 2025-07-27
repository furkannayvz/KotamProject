package com.example.demo.dto;

public class ChargingResponse {
    private boolean success;
    private String message;
    private String transactionId;
    private String msisdn;
    private int remainingBalance;
    private int initialBalance;
    private String transactionType;

    public ChargingResponse() {}

    public ChargingResponse(boolean success, String message, String msisdn, String transactionType) {
        this.success = success;
        this.message = message;
        this.msisdn = msisdn;
        this.transactionType = transactionType;
        this.transactionId = generateTransactionId();
    }

    private String generateTransactionId() {
        return "TXN_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getMsisdn() { return msisdn; }
    public void setMsisdn(String msisdn) { this.msisdn = msisdn; }
    
    public int getRemainingBalance() { return remainingBalance; }
    public void setRemainingBalance(int remainingBalance) { this.remainingBalance = remainingBalance; }

    public int getInitialBalance(){return initialBalance;}
    public void setInitialBalance(int initialBalance){this.initialBalance = initialBalance;}
    
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
}
