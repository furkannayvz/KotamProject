package org.sk.i2i.evren.TGF.dto;

import java.time.LocalDateTime;

public class SmsChargingRequest {
    private String senderMsisdn;
    private String receiverMsisdn;
    private int location;
    private LocalDateTime timestamp;

    public SmsChargingRequest() {
    }

    public SmsChargingRequest(String senderMsisdn, String receiverMsisdn, int location, LocalDateTime timestamp) {
        this.senderMsisdn = senderMsisdn;
        this.receiverMsisdn = receiverMsisdn;
        this.location = location;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getSenderMsisdn() {
        return senderMsisdn;
    }

    public void setSenderMsisdn(String senderMsisdn) {
        this.senderMsisdn = senderMsisdn;
    }

    public String getReceiverMsisdn() {
        return receiverMsisdn;
    }

    public void setReceiverMsisdn(String receiverMsisdn) {
        this.receiverMsisdn = receiverMsisdn;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SmsChargingRequest{" +
                "senderMsisdn='" + senderMsisdn + '\'' +
                ", receiverMsisdn='" + receiverMsisdn + '\'' +
                ", location=" + location +
                ", timestamp=" + timestamp +
                '}';
    }
}
