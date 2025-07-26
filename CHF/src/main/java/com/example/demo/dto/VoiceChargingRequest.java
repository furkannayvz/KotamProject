package com.example.demo.dto;

public class VoiceChargingRequest {
    private String callerMsisdn;
    private String calleeMsisdn;
    private int location;
    private int duration; // in seconds

    public VoiceChargingRequest() {}

    public VoiceChargingRequest(String callerMsisdn, String calleeMsisdn, int location, int duration) {
        this.callerMsisdn = callerMsisdn;
        this.calleeMsisdn = calleeMsisdn;
        this.location = location;
        this.duration = duration;
    }

    public String getCallerMsisdn() { return callerMsisdn; }
    public void setCallerMsisdn(String callerMsisdn) { this.callerMsisdn = callerMsisdn; }
    
    public String getCalleeMsisdn() { return calleeMsisdn; }
    public void setCalleeMsisdn(String calleeMsisdn) { this.calleeMsisdn = calleeMsisdn; }
    
    public int getLocation() { return location; }
    public void setLocation(int location) { this.location = location; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}
