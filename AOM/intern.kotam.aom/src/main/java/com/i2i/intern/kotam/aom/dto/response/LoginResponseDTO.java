// LoginResponseDTO.java
package com.i2i.intern.kotam.aom.dto.response;

import com.i2i.intern.kotam.aom.model.PackageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.i2i.intern.kotam.aom.model.Customer;


import java.sql.Timestamp;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor



public class LoginResponseDTO {
    private String msisdn;
    private String name;
    private String surname;
    private String email;
    private String nationalId;
    private Timestamp sDate;
    private PackageEntity packageEntity;

    public LoginResponseDTO(Customer customer, PackageEntity packageEntity) {
        this.msisdn = customer.getMsisdn();
        this.name = customer.getName();
        this.surname = customer.getSurname();
        this.email = customer.getEmail();
        this.nationalId = customer.getNationalId();
        this.sDate = customer.getsDate();
        this.packageEntity = packageEntity;
    }


}
