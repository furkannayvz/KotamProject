package com.i2i.kotam.model;

import java.util.Date;

public class NotificationMessage {

    public enum Type {
        VOICE, SMS, DATA
    }

    private String name;
    private String lastname;
    private String email;
    private String msisdn;
    private String threshold; // örneğin "80" veya "100"
    private String amount;    // toplam hak (örneğin 100 DK)
    private String remaining; // kalan hak
    private String packageName;
    private Date timestamp;   // kullanım zamanı
    private String startDate;
    private String endDate;
    private Type type;

    // Getters
    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getThreshold() {
        return threshold;
    }

    public String getAmount() {
        return amount;
    }

    public String getRemaining() {
        return remaining;
    }

    public String getPackageName() {
        return packageName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Type getType() {return type;}

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
