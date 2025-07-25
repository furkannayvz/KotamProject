package com.i2i.intern.kotam.aom.dto;

import java.sql.Timestamp;

public class BalanceDTO {
    private Long balanceId;
    private String msisdn;
    private Long leftData;
    private Long leftSms;
    private Long leftMinutes;
    private Timestamp getsDate;
    private PackageDTO packageEntity;

    // Getters and Setters
    public Long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Long getLeftData() {
        return leftData;
    }

    public void setLeftData(Long leftData) {
        this.leftData = leftData;
    }

    public Long getLeftSms() {
        return leftSms;
    }

    public void setLeftSms(Long leftSms) {
        this.leftSms = leftSms;
    }

    public Long getLeftMinutes() {
        return leftMinutes;
    }

    public void setLeftMinutes(Long leftMinutes) {
        this.leftMinutes = leftMinutes;
    }

    public Timestamp getGetsDate() {
        return getsDate;
    }

    public void setGetsDate(Timestamp getsDate) {
        this.getsDate = getsDate;
    }

    public PackageDTO getPackageEntity() {
        return packageEntity;
    }

    public void setPackageEntity(PackageDTO packageEntity) {
        this.packageEntity = packageEntity;
    }
}
