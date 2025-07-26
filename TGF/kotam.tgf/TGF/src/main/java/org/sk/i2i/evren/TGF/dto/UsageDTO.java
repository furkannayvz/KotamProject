package org.sk.i2i.evren.TGF.dto;


import java.time.LocalDateTime;

public class UsageDTO {

    private String msisdn;
    private String usageType;
    private int amount;
    private LocalDateTime usageTime;

    public UsageDTO() {
    }

    public UsageDTO(String msisdn, String usageType, int amount, LocalDateTime usageTime) {
        this.msisdn = msisdn;
        this.usageType = usageType;
        this.amount = amount;
        this.usageTime = usageTime;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(LocalDateTime usageTime) {
        this.usageTime = usageTime;
    }

    @Override
    public String toString() {
        return "UsageDTO{" +
                "msisdn='" + msisdn + '\'' +
                ", usageType='" + usageType + '\'' +
                ", amount=" + amount +
                ", usageTime=" + usageTime +
                '}';
    }
}
