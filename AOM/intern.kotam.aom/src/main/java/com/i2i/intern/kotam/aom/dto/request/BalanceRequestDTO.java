package com.i2i.intern.kotam.aom.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Bakiye oluşturmak için gerekli istek verilerini içerir")
public class BalanceRequestDTO {

    @Schema(description = "MSISDN (telefon numarası)", example = "5551234567")
    @JsonProperty("MSISDN")
    private String msisdn;

    @Schema(description = "Seçilen paket ID’si", example = "3")
    @JsonProperty("PACKAGE_ID")
    private Long packageId;

    @Override
    public String toString() {
        return "BalanceRequestDTO{" +
                ", msisdn='" + msisdn + '\'' +
                ", packageId=" + packageId +
                '}';
    }


    // Getter ve Setter'lar
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

}
