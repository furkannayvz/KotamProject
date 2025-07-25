package com.voltdb.controller;

import com.voltdb.dto.PackageDTO;
import com.voltdb.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/packages")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/name/by-id/{packageId}")
    @Operation(
            summary = "Package ismi bul",
            description = "Belirtilen Package ID'nin adını getirir.."
    )
    public ResponseEntity<String> getPackageNameById(@PathVariable int packageId) throws Exception{
        String name = packageService.getPackageNameByPackageId(packageId);
        return ResponseEntity.ok(name);
    }

    @GetMapping("/name/by-msisdn/{msisdn}")
    @Operation(
            summary = "Package ismi bul",
            description = "Belirtilen MSISDN'ye sahip kullanıcının Package adını getirir."
    )
    public ResponseEntity<String> getPackageNameByMsisdn(@PathVariable String msisdn) throws Exception{
        String name = packageService.getPackageNameByMsisdn(msisdn);
        return ResponseEntity.ok(name);
    }

    @GetMapping("/by-id/{packageId}")
    @Operation(
            summary = "Package bilgisi bul",
            description = "Belirtilen Package ID nin tüm bilgisini getirir."
    )
    public ResponseEntity<PackageDTO> getPackageInfoById(@PathVariable int packageId) throws Exception{
        PackageDTO dto = packageService.getPackageInfoById(packageId);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/by-msisdn/{msisdn}")
    @Operation(
            summary = "Package bilgisi bul",
            description = "Belirtilen MSISDN nin Package bilgisini getirir."
    )
    public ResponseEntity<PackageDTO> getPackageInfoByMsisdn(@PathVariable String msisdn) throws Exception{
        PackageDTO dto = packageService.getPackageInfoByMsisdn(msisdn);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }


}
