package net.i2i.kotam.model;

import java.util.Date;

public class NotificationMessage {

    private String name;
    private String lastname;
    private String msisdn;
    private String packageName;
    private Date timestamp;
    private String email;
    private String threshold;
    private String amount;
    private String remaining;
    private String startDate;
    private String endDate;
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

    public String getThreshold() { return threshold; }
    public void setThreshold(String threshold) { this.threshold = threshold; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public String getRemaining() { return remaining; }
    public void setRemaining(String remaining) { this.remaining = remaining; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }



        public Type getType() { return type; }
        public void setType(Type type) { this.type = type; }
    }


