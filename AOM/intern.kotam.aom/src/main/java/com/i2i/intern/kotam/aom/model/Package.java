package com.i2i.intern.kotam.aom.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Package {
    private Integer packageId;
    private Integer customerId;
    private String packageName;
    private Integer amountMinutes;
    private Integer amountSMS;
    private Integer amountData;
    private Double price;
    private Integer period;
    private Date createdDate;
    private Date updatedDate;

    // Parametreli constructor
    public Package(Integer packageId, Integer customerId, String packageName,
                   Integer amountMinutes, Integer amountSMS, Integer amountData,
                   Double price, Integer period, Date createdDate, Date updatedDate) {
        this.packageId = packageId;
        this.customerId = customerId;
        this.packageName = packageName;
        this.amountMinutes = amountMinutes;
        this.amountSMS = amountSMS;
        this.amountData = amountData;
        this.price = price;
        this.period = period;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // Boş constructor
    public Package() {}

    // Getter ve Setter metodları
    public Integer getPackageId() { return packageId; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public Integer getAmountMinutes() { return amountMinutes; }
    public void setAmountMinutes(Integer amountMinutes) { this.amountMinutes = amountMinutes; }

    public Integer getAmountSMS() { return amountSMS; }
    public void setAmountSMS(Integer amountSMS) { this.amountSMS = amountSMS; }

    public Integer getAmountData() { return amountData; }
    public void setAmountData(Integer amountData) { this.amountData = amountData; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getPeriod() { return period; }
    public void setPeriod(Integer period) { this.period = period; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public Date getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(Date updatedDate) { this.updatedDate = updatedDate; }

    @Override
    public String toString() {
        return "Package{" +
                "packageId=" + packageId +
                ", customerId=" + customerId +
                ", packageName='" + packageName + '\'' +
                ", amountMinutes=" + amountMinutes +
                ", amountSMS=" + amountSMS +
                ", amountData=" + amountData +
                ", price=" + price +
                ", period=" + period +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}