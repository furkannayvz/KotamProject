package com.i2i.intern.kotam.aom.enums;

public enum CustomerStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    BLOCKED("Blocked");

    private final String status;

    CustomerStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}