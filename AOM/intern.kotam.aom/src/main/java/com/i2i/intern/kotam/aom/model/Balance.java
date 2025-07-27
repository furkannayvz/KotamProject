package com.i2i.intern.kotam.aom.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

@Entity
@Table(name = "BALANCE")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BALANCE_ID") // VoltDB & Oracle uyumlu
    private Long balanceId;

    public Balance(){}

    public Balance(Long balanceId, String msisdn, Long leftData, Long leftSms, Long leftMinutes, Timestamp sDate, PackageEntity packageEntity) {
        this.balanceId = balanceId;
        this.msisdn = msisdn;
        this.leftData = leftData;
        this.leftSms = leftSms;
        this.leftMinutes = leftMinutes;
        this.sDate = sDate;
        this.packageEntity = packageEntity;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "balanceId=" + balanceId +
                ", msisdn='" + msisdn + '\'' +
                ", leftData=" + leftData +
                ", leftSms=" + leftSms +
                ", leftMinutes=" + leftMinutes +
                //", sDate=" + sDate +
                ", packageEntityId=" + (packageEntity != null ? packageEntity.getId() : "null") +
                '}';
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("BALANCE_ID", this.getBalanceId());
        json.put("MSISDN", this.getMsisdn());
        json.put("PACKAGE_ID", this.getPackageId());
        json.put("BAL_LEFT_MINUTES", this.getLeftMinutes());
        json.put("BAL_LEFT_SMS", this.getLeftSms());
        json.put("BAL_LEFT_DATA", this.getLeftData());
        json.put("SDATE", this.getsDate().toString());
        return json;
    }


    public static Balance fromJson(JSONObject json) {
        Balance balance = new Balance();
        balance.setBalanceId(json.getLong("BALANCE_ID"));
        balance.setMsisdn(json.getString("MSISDN"));
        balance.setPackageId(json.getLong("PACKAGE_ID"));
        balance.setLeftMinutes(json.getLong("BAL_LEFT_MINUTES"));
        balance.setLeftSms(json.getLong("BAL_LEFT_SMS"));
        balance.setLeftData(json.getLong("BAL_LEFT_DATA"));
        //balance.setsDate(Timestamp.valueOf(json.getString("SDATE")));

        String sdateStr = json.getString("SDATE"); // örnek: 2025-07-23T10:07:41.153+00:00

        // +00:00 formatını destekle
        OffsetDateTime odt = OffsetDateTime.parse(sdateStr);
        balance.setsDate(Timestamp.from(odt.toInstant()));

        return balance;
    }


    @Column(name = "MSISDN", nullable = false, length = 20)
    private String msisdn;

    @Column(name = "BAL_LEFT_DATA") // VoltDB: BAL_LEFT_DATA, Oracle uyumu
    private Long leftData;

    @Column(name = "BAL_LEFT_SMS") // VoltDB: BAL_LEFT_SMS
    private Long leftSms;

    @Column(name = "BAL_LEFT_MINUTES") // VoltDB: BAL_LEFT_MINUTES
    private Long leftMinutes;

    @Column(name = "SDATE") // VoltDB: SDATE
    private Timestamp sDate;

    @ManyToOne
    @JoinColumn(name = "PACKAGE_ID", nullable = false) // VoltDB: PACKAGE_ID
    private PackageEntity packageEntity;

    // Standard Getters and Setters
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

    public Long getLeftData() {
        return leftData;
    }

    public void setLeftData(Long leftData) {
        this.leftData = leftData;
    }

    public Long getLeftSms() {
        return leftSms;
    }

    public void setLeftSms(Long leftSms) {
        this.leftSms = leftSms;
    }

    public Long getLeftMinutes() {
        return leftMinutes;
    }

    public void setLeftMinutes(Long leftMinutes) {
        this.leftMinutes = leftMinutes;
    }



    public Timestamp getsDate() {
        return sDate;
    }

    public void setsDate(Timestamp sDate) {
        this.sDate = sDate;
    }

    public PackageEntity getPackageEntity() {
        return packageEntity;
    }

    public void setPackageEntity(PackageEntity packageEntity) {
        this.packageEntity = packageEntity;
    }

    public void setPackageId(Long packageId) {
        this.packageEntity = new PackageEntity();
        this.packageEntity.setId(packageId);
    }

    public Long getPackageId() {
        return (this.packageEntity != null) ? this.packageEntity.getId() : null;
    }

    // Convenience methods for Repository compatibility
    public Long getDataBalance() {
        return this.leftData;
    }

    public void setDataBalance(Long dataBalance) {
        this.leftData = dataBalance;
    }

    public Long getSmsBalance() {
        return this.leftSms;
    }

    public void setSmsBalance(Long smsBalance) {
        this.leftSms = smsBalance;
    }

    public Long getMinutesBalance() {
        return this.leftMinutes;
    }

    public void setMinutesBalance(Long minutesBalance) {
        this.leftMinutes = minutesBalance;
    }


    public void setLastUpdate(Timestamp lastUpdate) {
        this.sDate = lastUpdate;
    }

    public Timestamp getLastUpdate() {
        return this.sDate;
    }

    public Long getRemainingMinutes() {
        return this.leftMinutes;
    }

    public Long getRemainingSms() {
        return this.leftSms;
    }

    public Long getRemainingData() {
        return this.leftData;
    }

    public void setRemainingMinutes(Long m) {
        this.leftMinutes = m;
    }

    public void setRemainingSms(Long s) {
        this.leftSms = s;
    }

    public void setRemainingData(Long d) {
        this.leftData = d;
    }
}