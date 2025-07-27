package org.sk.i2i.evren.TGF.dto;

import java.time.LocalDateTime;

public class DataChargingRequest {
    private String msisdn;
    private int location;
    private int dataUsage;
    private int ratingGroup;
    private LocalDateTime timestamp;

    public DataChargingRequest() {
    }

    public DataChargingRequest(String msisdn, int location, int dataUsage, int ratingGroup, LocalDateTime timestamp) {
        this.msisdn = msisdn;
        this.location = location;
        this.dataUsage = dataUsage;
        this.ratingGroup = ratingGroup;
        this.timestamp = timestamp;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(int dataUsage) {
        this.dataUsage = dataUsage;
    }

    public int getRatingGroup() {
        return ratingGroup;
    }

    public void setRatingGroup(int ratingGroup) {
        this.ratingGroup = ratingGroup;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DataChargingRequest{" +
                "msisdn='" + msisdn + '\'' +
                ", location=" + location +
                ", dataUsage=" + dataUsage +
                ", ratingGroup=" + ratingGroup +
                ", timestamp=" + timestamp +
                '}';
    }
}
