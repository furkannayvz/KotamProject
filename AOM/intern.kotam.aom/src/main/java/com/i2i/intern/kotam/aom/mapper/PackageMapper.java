package com.i2i.intern.kotam.aom.mapper;

import com.i2i.intern.kotam.aom.dto.PackageDto;
import com.i2i.intern.kotam.aom.model.Package;
import com.i2i.intern.kotam.aom.repository.PackageRepository;
import org.springframework.stereotype.Component;

@Component
public class PackageMapper {

    public PackageDto packageToPackageDto(Package packageModel) {
        if (packageModel == null) {
            return null;
        }

        return PackageDto.builder()
                .packageId(packageModel.getPackageId())
                .packageName(packageModel.getPackageName())
                .amountMinutes(packageModel.getAmountMinutes())
                .amountSms(packageModel.getAmountSMS())
                .amountData(packageModel.getAmountData())
                .price(packageModel.getPrice())
                .period(packageModel.getPeriod())
                .status("ACTIVE") // Default status
                .description(buildPackageDescription(packageModel))
                .build();
    }

    public PackageDto voltPackageToPackageDto(PackageRepository.VoltPackageInfo voltPackageInfo) {
        if (voltPackageInfo == null) {
            return null;
        }

        return PackageDto.builder()
                .packageName(voltPackageInfo.getPackageName())
                .amountMinutes(voltPackageInfo.getAmountMinutes())
                .amountSms(voltPackageInfo.getAmountSms())
                .amountData(voltPackageInfo.getAmountData())
                .price(voltPackageInfo.getPrice())
                .status("ACTIVE")
                .description(buildVoltPackageDescription(voltPackageInfo))
                .build();
    }

    private String buildPackageDescription(Package packageModel) {
        return String.format("%s - %d minutes, %d SMS, %d MB data for %d days",
                packageModel.getPackageName(),
                packageModel.getAmountMinutes(),
                packageModel.getAmountSMS(),
                packageModel.getAmountData(),
                packageModel.getPeriod());
    }

    private String buildVoltPackageDescription(PackageRepository.VoltPackageInfo voltPackageInfo) {
        return String.format("%s - %d minutes, %d SMS, %d MB data",
                voltPackageInfo.getPackageName(),
                voltPackageInfo.getAmountMinutes(),
                voltPackageInfo.getAmountSms(),
                voltPackageInfo.getAmountData());
    }
}
