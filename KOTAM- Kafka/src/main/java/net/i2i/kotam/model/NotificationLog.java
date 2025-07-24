package net.i2i.kotam.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class NotificationLog {
    @JsonProperty("NOTIFICATION_ID")
    private int NOTIFICATION_ID;

    @JsonProperty("NOTIFICATION_TYPE") // 'EMAIL' or 'SMS'
    private String NOTIFICATION_TYPE;

    @JsonProperty("NOTIFICATION_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime NOTIFICATION_TIME;

    @JsonProperty("MSISDN")
    private String MSISDN;

    // Constructors

    public NotificationLog() {
        this.NOTIFICATION_TIME = LocalDateTime.now();
    }

    public NotificationLog(int NOTIFICATION_ID, String NOTIFICATION_TYPE, String MSISDN) {
        this.NOTIFICATION_ID = NOTIFICATION_ID;
        this.NOTIFICATION_TYPE = NOTIFICATION_TYPE;
        this.MSISDN = MSISDN;
        this.NOTIFICATION_TIME = LocalDateTime.now();
    }

    // Getters and Setters

    public int getNOTIFICATION_ID() {
        return NOTIFICATION_ID;
    }

    public void setNOTIFICATION_ID(int NOTIFICATION_ID) {
        this.NOTIFICATION_ID = NOTIFICATION_ID;
    }

    public String getNOTIFICATION_TYPE() {
        return NOTIFICATION_TYPE;
    }

    public void setNOTIFICATION_TYPE(String NOTIFICATION_TYPE) {
        this.NOTIFICATION_TYPE = NOTIFICATION_TYPE;
    }

    public LocalDateTime getNOTIFICATION_TIME() {
        return NOTIFICATION_TIME;
    }

    public void setNOTIFICATION_TIME(LocalDateTime NOTIFICATION_TIME) {
        this.NOTIFICATION_TIME = NOTIFICATION_TIME;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    @Override
    public String toString() {
        return "NotificationLog{" +
                "NOTIFICATION_ID=" + NOTIFICATION_ID +
                ", NOTIFICATION_TYPE='" + NOTIFICATION_TYPE + '\'' +
                ", NOTIFICATION_TIME=" + NOTIFICATION_TIME +
                ", MSISDN='" + MSISDN + '\'' +
                '}';
    }
}
