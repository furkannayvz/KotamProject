package com.i2i.intern.kotam.aom.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private Integer customerId;
    private String msisdn;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String TCNumber;
    private Date createdDate;
    private Date updatedDate;
    private Boolean isActive;
    private Timestamp sDate;

    // Helper methods
    public String getFullName() {
        return (name != null ? name : "") +
                (surname != null ? " " + surname : "");
    }

    public Boolean getIsActive() {
        return isActive != null ? isActive : false;
    }
}