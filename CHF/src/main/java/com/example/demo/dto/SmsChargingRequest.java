package com.example.demo.dto;

public class SmsChargingRequest {
    private String senderMsisdn;
    private String receiverMsisdn;
    private int location;
    private int smsCount = 1; // default 1 SMS

    public SmsChargingRequest() {}

    public SmsChargingRequest(String senderMsisdn, String receiverMsisdn, int location) {
        this.senderMsisdn = senderMsisdn;
        this.receiverMsisdn = receiverMsisdn;
        this.location = location;
    }

    public String getSenderMsisdn() { return senderMsisdn; }
    public void setSenderMsisdn(String senderMsisdn) { this.senderMsisdn = senderMsisdn; }
    
    public String getReceiverMsisdn() { return receiverMsisdn; }
    public void setReceiverMsisdn(String receiverMsisdn) { this.receiverMsisdn = receiverMsisdn; }
    
    public int getLocation() { return location; }
    public void setLocation(int location) { this.location = location; }
    
    public int getSmsCount() { return smsCount; }
    public void setSmsCount(int smsCount) { this.smsCount = smsCount; }
}