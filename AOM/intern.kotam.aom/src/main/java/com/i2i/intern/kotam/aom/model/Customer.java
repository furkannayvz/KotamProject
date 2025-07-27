package com.i2i.intern.kotam.aom.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@Entity
@Table(name = "CUSTOMER")
@Schema(description = "Müşteri bilgilerini temsil eder")
public class Customer {

    @Id
    @Column(name = "MSISDN", length = 20)
    @Schema(description = "Müşterinin telefon numarası", example = "5551234567")
    private String msisdn;

    @Column(name = "NAME", nullable = false, length = 50)
    @Schema(description = "Müşteri adı", example = "Zeliha")
    private String name;

    @Column(name = "SURNAME", nullable = false, length = 50)
    @Schema(description = "Müşteri soyadı", example = "Polat")
    private String surname;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    @Schema(description = "E-posta adresi", example = "zeliha@example.com")
    private String email;

    @Column(name = "PASSWORD", nullable = false, length = 100)
    @Schema(hidden = true)
    private String password;

    @Column(name = "SDATE", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Kayıt tarihi", example = "2024-07-26T12:00:00Z")
    private Timestamp sDate;

    @Column(name = "NATIONAL_ID", nullable = false, unique = true, length = 11)
    @Schema(description = "T.C. kimlik numarası", example = "12345678901")
    private String nationalId;

    @ManyToOne
    @JoinColumn(name = "PACKAGE_ID", nullable = false)
    private PackageEntity packageEntity;

    // GETTER ve SETTER
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
