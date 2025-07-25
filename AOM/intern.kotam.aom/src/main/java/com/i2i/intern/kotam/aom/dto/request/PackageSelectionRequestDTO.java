package com.i2i.intern.kotam.aom.dto.request;

public class PackageSelectionRequestDTO {
    private String msisdn;
    private Long packageId;

    // getter, setter


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
}
