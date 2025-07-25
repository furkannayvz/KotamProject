package com.i2i.intern.kotam.aom.dto;

public class PackageDTO {
    private Long id;
    private int dataQuota;
    private int smsQuota;
    private int minutesQuota;
    private double price;
    private int period;
    private String packageName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDataQuota() {
        return dataQuota;
    }

    public void setDataQuota(int dataQuota) {
        this.dataQuota = dataQuota;
    }

    public int getSmsQuota() {
        return smsQuota;
    }

    public void setSmsQuota(int smsQuota) {
        this.smsQuota = smsQuota;
    }

    public int getMinutesQuota() {
        return minutesQuota;
    }

    public void setMinutesQuota(int minutesQuota) {
        this.minutesQuota = minutesQuota;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}