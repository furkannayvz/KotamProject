// LoginResponseDTO.java
package com.i2i.intern.kotam.aom.dto.response;

import com.i2i.intern.kotam.aom.model.PackageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Kullanıcının giriş işlemi sonrası dönen bilgiler")
public class LoginResponseDTO {

    @Schema(description = "MSISDN (telefon numarası)", example = "5551234567")
    private String msisdn;

    @Schema(description = "Müşteri adı", example = "Zeliha")
    private String name;

    @Schema(description = "Müşteri soyadı", example = "Polat")
    private String surname;

    @Schema(description = "Müşteri e-posta adresi", example = "zeliha@example.com")
    private String email;

    @Schema(description = "Müşteri T.C. kimlik numarası", example = "12345678901")
    private String nationalId;

    @Schema(description = "Kayıt tarihi", example = "2024-07-26T13:00:00Z")
    private Timestamp sDate;

    @Schema(description = "Seçilen paketin bilgileri")
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
