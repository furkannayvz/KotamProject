package com.i2i.intern.kotam.aom.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceRequestDTO {

    @JsonProperty("BALANCE_ID")
    private Long balanceId;

    @JsonProperty("MSISDN")
    private String msisdn;

    @JsonProperty("PACKAGE_ID")
    private Long packageId;

    @JsonProperty("BAL_LEFT_MINUTES")
    private Long leftMinutes;

    @JsonProperty("BAL_LEFT_SMS")
    private Long leftSms;

    @JsonProperty("BAL_LEFT_DATA")
    private Long leftData;
    //private String sDate;

    @Override
    public String toString() {
        return "BalanceRequestDTO{" +
                "balanceId=" + balanceId +
                ", msisdn='" + msisdn + '\'' +
                ", packageId=" + packageId +
                ", leftMinutes=" + leftMinutes +
                ", leftSms=" + leftSms +
                ", leftData=" + leftData +
                //", sDate='" + sDate + '\'' +
                '}';
    }

    // Getter ve Setter'lar
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

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Long getLeftMinutes() {
        return leftMinutes;
    }

    public void setLeftMinutes(Long leftMinutes) {
        this.leftMinutes = leftMinutes;
    }

    public Long getLeftSms() {
        return leftSms;
    }

    public void setLeftSms(Long leftSms) {
        this.leftSms = leftSms;
    }

    public Long getLeftData() {
        return leftData;
    }

    public void setLeftData(Long leftData) {
        this.leftData = leftData;
    }

    /*
    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }*/

}
