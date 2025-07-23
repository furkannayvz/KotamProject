package com.i2i.intern.kotam.aom.dto.request;


public class PackageRequestDTO {
    private Long packageId;
    private String packageName;

    // Getters and Setters
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}

