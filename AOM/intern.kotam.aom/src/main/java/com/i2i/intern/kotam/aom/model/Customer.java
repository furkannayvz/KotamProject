package com.i2i.intern.kotam.aom.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @Column(name = "MSISDN", length = 20)
    private String msisdn;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "SURNAME", nullable = false, length = 50)
    private String surname;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;

    @Column(name = "SDATE", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp sDate;

    @Column(name = "NATIONAL_ID", nullable = false, unique = true, length = 11)
    private String nationalId;

    @ManyToOne
    @JoinColumn(name = "PACKAGE_ID", nullable = false)
    private PackageEntity packageEntity;

    // === GETTER and SETTER ===

    public Customer(){}

    public Customer(String msisdn, String name, String surname, String email, String password, Timestamp sDate, String nationalId, PackageEntity packageEntity) {
        this.msisdn = msisdn;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.sDate = sDate;
        this.nationalId = nationalId;
        this.packageEntity = packageEntity;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getsDate() {
        return sDate;
    }

    public void setsDate(Timestamp sDate) {
        this.sDate = sDate;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public PackageEntity getPackageEntity() {
        return packageEntity;
    }

    public void setPackageEntity(PackageEntity packageEntity) {
        this.packageEntity = packageEntity;
    }
}
