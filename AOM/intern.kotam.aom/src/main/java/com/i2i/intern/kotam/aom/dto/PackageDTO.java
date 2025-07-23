package com.i2i.intern.kotam.aom.dto;

public class PackageDTO {

    private Long id;
    private String name;
    private Long dataQuota;
    private Long smsQuota;
    private Long minutesQuota;
    private boolean isActive;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDataQuota() {
        return dataQuota;
    }

    public void setDataQuota(Long dataQuota) {
        this.dataQuota = dataQuota;
    }

    public Long getSmsQuota() {
        return smsQuota;
    }

    public void setSmsQuota(Long smsQuota) {
        this.smsQuota = smsQuota;
    }

    public Long getMinutesQuota() {
        return minutesQuota;
    }

    public void setMinutesQuota(Long minutesQuota) {
        this.minutesQuota = minutesQuota;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
