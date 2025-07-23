package net.i2i.kotam.model;

import java.util.Date;

public class NotificationMessage {

    private String name;
    private String lastname;
    private String msisdn;
    private String packageName;
    private Date validUntil;
    private int voiceMinutes;
    private int voiceSeconds;
    private int dataMb;
    private int smsCount;
    private int usedAmount;
    private int totalAmount;
    private Type type;

    public enum Type {
        VOICE, SMS, DATA
    }

    public String getName() { return name; }
        public void setName(String name) { this.name = name; }

       public String getLastname() { return lastname; }
      public void setLastname(String lastname) { this.lastname = lastname; }

       public String getMsisdn() { return msisdn; }
        public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

        public String getPackageName() { return packageName; }
        public void setPackageName(String packageName) { this.packageName = packageName; }

       public Date getValidUntil() { return validUntil; }
        public void setValidUntil(Date validUntil) { this.validUntil = validUntil; }

       public int getVoiceMinutes() { return voiceMinutes; }
      public void setVoiceMinutes(int voiceMinutes) { this.voiceMinutes = voiceMinutes; }

       public int getVoiceSeconds() { return voiceSeconds; }
       public void setVoiceSeconds(int voiceSeconds) { this.voiceSeconds = voiceSeconds; }

       public int getDataMb() { return dataMb; }
        public void setDataMb(int dataMb) { this.dataMb = dataMb; }

        public int getSmsCount() { return smsCount; }
        public void setSmsCount(int smsCount) { this.smsCount = smsCount; }

        public int getUsedAmount() { return usedAmount; }
       public void setUsedAmount(int usedAmount) { this.usedAmount = usedAmount; }

        public int getTotalAmount() { return totalAmount; }
        public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

        public Type getType() { return type; }
        public void setType(Type type) { this.type = type; }
    }


