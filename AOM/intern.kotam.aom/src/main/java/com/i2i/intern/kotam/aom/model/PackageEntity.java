package com.i2i.intern.kotam.aom.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PACKAGE_DETAILS")
@Schema(description = "Paket veritabanı varlığını temsil eder")
public class PackageEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @Schema(description = "Paketin benzersiz kimliği", example = "1")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    @Schema(description = "Paket adı", example = "Sosyal Paket")
    private String name;

    @Column(name = "DATA_QUOTA")
    @Schema(description = "Veri kotası (MB)", example = "5000")
    private Long dataQuota;

    @Column(name = "SMS_QUOTA")
    @Schema(description = "SMS kotası", example = "1000")
    private Long smsQuota;

    @Column(name = "MINUTES_QUOTA")
    @Schema(description = "Dakika kotası", example = "500")
    private Long minutesQuota;

    @Column(name = "PRICE")
    @Schema(description = "Paket fiyatı (₺)", example = "39.90")
    private Double price;

    @Schema(description = "Paket geçerlilik süresi (gün)", example = "30")
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
        //this.isActive = isActive;
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