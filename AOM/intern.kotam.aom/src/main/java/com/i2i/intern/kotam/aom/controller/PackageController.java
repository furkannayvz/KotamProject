package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.model.PackageEntity;
import com.i2i.intern.kotam.aom.service.PackageServiceOracle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for Package-related operations (Oracle)
 */
@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageServiceOracle packageService;

    public PackageController(PackageServiceOracle packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PackageEntity>> getAllPackages() {
        List<PackageEntity> packages = packageService.getAllPackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PackageEntity> getPackageById(@PathVariable Long id) {
        Optional<PackageEntity> pkg = packageService.getPackageDetailsById(id);
        return pkg.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PackageEntity> getPackageByName(@PathVariable String name) {
        Optional<PackageEntity> pkg = packageService.getPackageDetailsByName(name);
        return pkg.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/id-by-name/{name}")
    public ResponseEntity<Long> getPackageIdByName(@PathVariable String name) {
        Optional<Long> id = packageService.getPackageIdByName(name);
        return id.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
