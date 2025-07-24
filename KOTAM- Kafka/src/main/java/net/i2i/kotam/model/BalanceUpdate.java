package net.i2i.kotam.model;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class BalanceUpdate {
    @JsonProperty("BALANCE_ID")
    private int BALANCE_ID;

    @JsonProperty("MSISDN")
    private String MSISDN;

    @JsonProperty("PACKAGE_ID")
    private int PACKAGE_ID;

    @JsonProperty("BAL_LEFT_MINUTES")
    private int BAL_LEFT_MINUTES;

    @JsonProperty("BAL_LEFT_SMS")
    private int BAL_LEFT_SMS;

    @JsonProperty("BAL_LEFT_DATA")
    private int BAL_LEFT_DATA; // in MB

    @JsonProperty("SDATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime SDATE;



    // Constructors, Getters and Setters

    public BalanceUpdate() {
        this.SDATE = LocalDateTime.now();
    }

    public BalanceUpdate(int BALANCE_ID, String MSISDN, int PACKAGE_ID, int BAL_LEFT_MINUTES, int BAL_LEFT_SMS, int BAL_LEFT_DATA) {
        this.BALANCE_ID = BALANCE_ID;
        this.MSISDN = MSISDN;
        this.PACKAGE_ID = PACKAGE_ID;
        this.BAL_LEFT_MINUTES = BAL_LEFT_MINUTES;
        this.BAL_LEFT_SMS = BAL_LEFT_SMS;
        this.BAL_LEFT_DATA = BAL_LEFT_DATA;
        this.SDATE = LocalDateTime.now();
    }

    public int getBALANCE_ID() {
        return BALANCE_ID;
    }

    public void setBALANCE_ID(int BALANCE_ID) {
        this.BALANCE_ID = BALANCE_ID;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    public int getPACKAGE_ID() {
        return PACKAGE_ID;
    }

    public void setPACKAGE_ID(int PACKAGE_ID) {
        this.PACKAGE_ID = PACKAGE_ID;
    }

    public int getBAL_LEFT_MINUTES() {
        return BAL_LEFT_MINUTES;
    }

    public void setBAL_LEFT_MINUTES(int BAL_LEFT_MINUTES) {
        this.BAL_LEFT_MINUTES = BAL_LEFT_MINUTES;
    }

    public int getBAL_LEFT_SMS() {
        return BAL_LEFT_SMS;
    }

    public void setBAL_LEFT_SMS(int BAL_LEFT_SMS) {
        this.BAL_LEFT_SMS = BAL_LEFT_SMS;
    }

    public int getBAL_LEFT_DATA() {
        return BAL_LEFT_DATA;
    }

    public void setBAL_LEFT_DATA(int BAL_LEFT_DATA) {
        this.BAL_LEFT_DATA = BAL_LEFT_DATA;
    }

    public LocalDateTime getSDATE() {
        return SDATE;
    }

    public void setSDATE(LocalDateTime SDATE) {
        this.SDATE = SDATE;
    }

    @Override
    public String toString() {
        return "BalanceUpdate{" +
                "BALANCE_ID=" + BALANCE_ID +
                ", MSISDN='" + MSISDN + '\'' +
                ", PACKAGE_ID=" + PACKAGE_ID +
                ", BAL_LEFT_MINUTES=" + BAL_LEFT_MINUTES +
                ", BAL_LEFT_SMS=" + BAL_LEFT_SMS +
                ", BAL_LEFT_DATA=" + BAL_LEFT_DATA +
                ", SDATE=" + SDATE +
                '}';
    }
}
