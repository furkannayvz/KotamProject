package net.i2i.kotam.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class PackageInfo {
    private int PACKAGE_ID;
    private String PACKAGE_NAME;
    private double PRICE;
    private int AMOUNT_MINUTES;
    private int AMOUNT_DATA; // in MB
    private int AMOUNT_SMS;
    private int PERIOD; // in days



    // Constructors, Getters and Setters

    public PackageInfo() {
    }

    public PackageInfo(int PACKAGE_ID, String PACKAGE_NAME, double PRICE, int AMOUNT_MINUTES, int AMOUNT_DATA, int AMOUNT_SMS, int PERIOD) {
        this.PACKAGE_ID = PACKAGE_ID;
        this.PACKAGE_NAME = PACKAGE_NAME;
        this.PRICE = PRICE;
        this.AMOUNT_MINUTES = AMOUNT_MINUTES;
        this.AMOUNT_DATA = AMOUNT_DATA;
        this.AMOUNT_SMS = AMOUNT_SMS;
        this.PERIOD = PERIOD;
    }

    public int getPACKAGE_ID() {
        return PACKAGE_ID;
    }

    public void setPACKAGE_ID(int PACKAGE_ID) {
        this.PACKAGE_ID = PACKAGE_ID;
    }

    public String getPACKAGE_NAME() {
        return PACKAGE_NAME;
    }

    public void setPACKAGE_NAME(String PACKAGE_NAME) {
        this.PACKAGE_NAME = PACKAGE_NAME;
    }

    public double getPRICE() {
        return PRICE;
    }

    public void setPRICE(double PRICE) {
        this.PRICE = PRICE;
    }

    public int getAMOUNT_MINUTES() {
        return AMOUNT_MINUTES;
    }

    public void setAMOUNT_MINUTES(int AMOUNT_MINUTES) {
        this.AMOUNT_MINUTES = AMOUNT_MINUTES;
    }

    public int getAMOUNT_DATA() {
        return AMOUNT_DATA;
    }

    public void setAMOUNT_DATA(int AMOUNT_DATA) {
        this.AMOUNT_DATA = AMOUNT_DATA;
    }

    public int getAMOUNT_SMS() {
        return AMOUNT_SMS;
    }

    public void setAMOUNT_SMS(int AMOUNT_SMS) {
        this.AMOUNT_SMS = AMOUNT_SMS;
    }

    public int getPERIOD() {
        return PERIOD;
    }

    public void setPERIOD(int PERIOD) {
        this.PERIOD = PERIOD;
    }

    @Override
    public String toString() {
        return "PackageInfo{" +
                "PACKAGE_ID=" + PACKAGE_ID +
                ", PACKAGE_NAME='" + PACKAGE_NAME + '\'' +
                ", PRICE=" + PRICE +
                ", AMOUNT_MINUTES=" + AMOUNT_MINUTES +
                ", AMOUNT_DATA=" + AMOUNT_DATA +
                ", AMOUNT_SMS=" + AMOUNT_SMS +
                ", PERIOD=" + PERIOD +
                '}';
    }
}