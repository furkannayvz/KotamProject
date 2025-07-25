package com.i2i.intern.kotam.aom.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceRequestDTO {



    @JsonProperty("MSISDN")
    private String msisdn;

    @JsonProperty("PACKAGE_ID")
    private Long packageId;







    @Override
    public String toString() {
        return "BalanceRequestDTO{" +
                ", msisdn='" + msisdn + '\'' +
                ", packageId=" + packageId +
                '}';
    }

    // Getter ve Setter'lar

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

    /*
    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }*/

}
