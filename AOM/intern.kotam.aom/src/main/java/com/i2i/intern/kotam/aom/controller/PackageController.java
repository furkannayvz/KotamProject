package com.i2i.intern.kotam.aom.controller;

import com.i2i.intern.kotam.aom.dto.PackageDetails;
import com.i2i.intern.kotam.aom.dto.PackageDto;
import com.i2i.intern.kotam.aom.service.PackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for Package related operations
 */
@RestController
@RequestMapping("/v1/api/packages")
public class PackageController {

    private static final Logger logger = LoggerFactory.getLogger(PackageController.class);
    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/getAllPackages")
    public ResponseEntity<List<PackageDto>> retrieveAllAvailablePackages() {

        logger.info("Retrieving all available packages");

        List<PackageDto> packages = packageService.retrieveAllAvailablePackages();

        logger.info("All packages retrieved successfully, count: {}", packages.size());
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/getUserPackageByMsisdn")
    public ResponseEntity<PackageDto> fetchCustomerPackageByMsisdn(@RequestParam String msisdn) {

        logger.info("Fetching customer package by MSISDN: {}", msisdn);

        PackageDto packageDto = packageService.fetchCustomerPackageByMsisdn(msisdn);

        logger.info("Customer package fetched successfully for MSISDN: {}", msisdn);
        return ResponseEntity.ok(packageDto);
    }

    @GetMapping("/getPackageDetails")
    public ResponseEntity<PackageDetails> searchPackageDetailsByName(@RequestParam String packageName)
            throws SQLException, ClassNotFoundException {

        logger.info("Searching package details for package: {}", packageName);

        Optional<PackageDetails> packageDetails = packageService.searchPackageDetailsByName(packageName);

        if (packageDetails.isPresent()) {
            logger.info("Package details found for package: {}", packageName);
            return ResponseEntity.ok(packageDetails.get());
        } else {
            logger.warn("Package details not found for package: {}", packageName);
            return ResponseEntity.notFound().build();
        }
    }
}