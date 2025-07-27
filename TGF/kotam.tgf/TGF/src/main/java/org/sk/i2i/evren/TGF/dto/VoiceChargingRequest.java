package org.sk.i2i.evren.TGF.dto;

import java.time.LocalDateTime;

public class VoiceChargingRequest {
    private String callerMsisdn;
    private String calleeMsisdn;
    private int location;
    private int duration;
    private LocalDateTime timestamp;

    public VoiceChargingRequest() {
    }

    public VoiceChargingRequest(String callerMsisdn, String calleeMsisdn, int location, int duration, LocalDateTime timestamp) {
        this.callerMsisdn = callerMsisdn;
        this.calleeMsisdn = calleeMsisdn;
        this.location = location;
        this.duration = duration;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getCallerMsisdn() {
        return callerMsisdn;
    }

    public void setCallerMsisdn(String callerMsisdn) {
        this.callerMsisdn = callerMsisdn;
    }

    public String getCalleeMsisdn() {
        return calleeMsisdn;
    }

    public void setCalleeMsisdn(String calleeMsisdn) {
        this.calleeMsisdn = calleeMsisdn;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "VoiceChargingRequest{" +
                "callerMsisdn='" + callerMsisdn + '\'' +
                ", calleeMsisdn='" + calleeMsisdn + '\'' +
                ", location=" + location +
                ", duration=" + duration +
                ", timestamp=" + timestamp +
                '}';
    }
}
