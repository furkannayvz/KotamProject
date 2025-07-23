package com.i2i.intern.kotam.aom.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PACKAGE_DETAILS")
public class PackageEntity implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "DATA_QUOTA")
    private Long dataQuota;

    @Column(name = "SMS_QUOTA")
    private Long smsQuota;

    @Column(name = "MINUTES_QUOTA")
    private Long minutesQuota;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "PERIOD") // Yeni alan
    private Integer period;

    public PackageEntity(){}

    public PackageEntity(Long id, String name, Long dataQuota, Long smsQuota, Long minutesQuota, Double price, Boolean isActive, Integer period) {
        this.id = id;
        this.name = name;
        this.dataQuota = dataQuota;
        this.smsQuota = smsQuota;
        this.minutesQuota = minutesQuota;
        this.price = price;
        this.isActive = isActive;
        this.period = period;
    }
// Getters & Setters

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public void setPackageName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return this.name;
    }

}
