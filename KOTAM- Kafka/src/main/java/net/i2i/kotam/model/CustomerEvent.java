package net.i2i.kotam.model;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class CustomerEvent {
    private String MSISDN;
    private String NAME;
    private String SURNAME;
    private String EMAIL;
    private String PASSWORD;
    private String NATIONAL_ID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime SDATE;



    // Constructors, Getters and Setters

    public CustomerEvent() {
        this.SDATE = LocalDateTime.now();
    }

    public CustomerEvent(String MSISDN, String NAME, String SURNAME, String EMAIL, String PASSWORD, String NATIONAL_ID) {
        this.MSISDN = MSISDN;
        this.NAME = NAME;
        this.SURNAME = SURNAME;
        this.EMAIL = EMAIL;
        this.PASSWORD = PASSWORD;
        this.NATIONAL_ID = NATIONAL_ID;
        this.SDATE = LocalDateTime.now();
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getSURNAME() {
        return SURNAME;
    }

    public void setSURNAME(String SURNAME) {
        this.SURNAME = SURNAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getNATIONAL_ID() {
        return NATIONAL_ID;
    }

    public void setNATIONAL_ID(String NATIONAL_ID) {
        this.NATIONAL_ID = NATIONAL_ID;
    }

    public LocalDateTime getSDATE() {
        return SDATE;
    }

    public void setSDATE(LocalDateTime SDATE) {
        this.SDATE = SDATE;
    }

    @Override
    public String toString() {
        return "CustomerEvent{" +
                "MSISDN='" + MSISDN + '\'' +
                ", NAME='" + NAME + '\'' +
                ", SURNAME='" + SURNAME + '\'' +
                ", EMAIL='" + EMAIL + '\'' +
                ", PASSWORD='" + PASSWORD + '\'' +
                ", NATIONAL_ID='" + NATIONAL_ID + '\'' +
                ", SDATE=" + SDATE +
                '}';
    }
}
