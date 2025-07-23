package net.i2i.kotam.model;



import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class UsageEvent {
    private int PERSONAL_USAGE_ID;
    private String GIVER_ID;
    private String RECEIVER_ID;
    private String USAGE_TYPE; // e.g., 'CALL', 'SMS', 'INTERNET'
    private int USAGE_DURATION; // minutes/seconds for calls, count for SMS, MB for internet
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime USAGE_DATE;

    // Constructors, Getters and Setters

    public UsageEvent() {
        this.USAGE_DATE = LocalDateTime.now();
    }

    public UsageEvent(int PERSONAL_USAGE_ID, String GIVER_ID, String RECEIVER_ID, String USAGE_TYPE, int USAGE_DURATION) {
        this.PERSONAL_USAGE_ID = PERSONAL_USAGE_ID;
        this.GIVER_ID = GIVER_ID;
        this.RECEIVER_ID = RECEIVER_ID;
        this.USAGE_TYPE = USAGE_TYPE;
        this.USAGE_DURATION = USAGE_DURATION;
        this.USAGE_DATE = LocalDateTime.now();
    }

    public int getPERSONAL_USAGE_ID() {
        return PERSONAL_USAGE_ID;
    }

    public void setPERSONAL_USAGE_ID(int PERSONAL_USAGE_ID) {
        this.PERSONAL_USAGE_ID = PERSONAL_USAGE_ID;
    }

    public String getGIVER_ID() {
        return GIVER_ID;
    }

    public void setGIVER_ID(String GIVER_ID) {
        this.GIVER_ID = GIVER_ID;
    }

    public String getRECEIVER_ID() {
        return RECEIVER_ID;
    }

    public void setRECEIVER_ID(String RECEIVER_ID) {
        this.RECEIVER_ID = RECEIVER_ID;
    }

    public String getUSAGE_TYPE() {
        return USAGE_TYPE;
    }

    public void setUSAGE_TYPE(String USAGE_TYPE) {
        this.USAGE_TYPE = USAGE_TYPE;
    }

    public int getUSAGE_DURATION() {
        return USAGE_DURATION;
    }

    public void setUSAGE_DURATION(int USAGE_DURATION) {
        this.USAGE_DURATION = USAGE_DURATION;
    }

    public LocalDateTime getUSAGE_DATE() {
        return USAGE_DATE;
    }

    public void setUSAGE_DATE(LocalDateTime USAGE_DATE) {
        this.USAGE_DATE = USAGE_DATE;
    }

    @Override
    public String toString() {
        return "UsageEvent{" +
                "PERSONAL_USAGE_ID=" + PERSONAL_USAGE_ID +
                ", GIVER_ID='" + GIVER_ID + '\'' +
                ", RECEIVER_ID='" + RECEIVER_ID + '\'' +
                ", USAGE_TYPE='" + USAGE_TYPE + '\'' +
                ", USAGE_DURATION=" + USAGE_DURATION +
                ", USAGE_DATE=" + USAGE_DATE +
                '}';
    }
}
