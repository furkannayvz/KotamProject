package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.mapper.PackageMapper;
import com.i2i.intern.kotam.aom.dto.PackageDetails;
import com.i2i.intern.kotam.aom.dto.PackageDto;
import com.i2i.intern.kotam.aom.model.Package;
import com.i2i.intern.kotam.aom.repository.PackageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.voltdb.client.ProcCallException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PackageService {

    private static final Logger logger = LoggerFactory.getLogger(PackageService.class);

    private final PackageRepository packageRepository;
    private final PackageMapper packageMapper;

    public PackageService(PackageRepository packageRepository,
                          PackageMapper packageMapper) {
        this.packageRepository = packageRepository;
        this.packageMapper = packageMapper;
    }

    public List<PackageDto> retrieveAllAvailablePackages() {
        logger.info("Retrieving all available packages from database");

        try {
            List<Package> packageList = packageRepository.retrieveAllPackagesFromOracle();
            logger.debug("Retrieved {} packages from database", packageList.size());

            return convertPackagesToDtoList(packageList);

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Database error occurred while retrieving packages", e);
            throw new RuntimeException("Failed to retrieve packages from database", e);
        }
    }

    public PackageDto fetchCustomerPackageByMsisdn(String msisdn) {
        logger.info("Fetching customer package for MSISDN: {}", msisdn);

        try {
            PackageRepository.VoltPackageInfo voltPackageInfo = packageRepository.fetchUserPackageByMsisdn(msisdn);
            logger.debug("Customer package retrieved successfully for MSISDN: {}", msisdn);

            return transformVoltPackageToDto(voltPackageInfo);

        } catch (IOException | ProcCallException exception) {
            logger.error("VoltDB error occurred while fetching customer package for MSISDN: {}", msisdn, exception);
            throw new RuntimeException("Failed to fetch customer package for MSISDN: " + msisdn, exception);
        }
    }

    public Optional<PackageDetails> searchPackageDetailsByName(String packageName) {
        logger.info("Searching package details for package: {}", packageName);

        try {
            Optional<PackageDetails> packageDetails = packageRepository.searchPackageDetailsByName(packageName);

            if (packageDetails.isPresent()) {
                logger.debug("Package details found for package: {}", packageName);
            } else {
                logger.warn("Package details not found for package: {}", packageName);
            }

            return packageDetails;

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Database error occurred while searching package details for: {}", packageName, e);
            throw new RuntimeException("Failed to search package details for: " + packageName, e);
        }
    }

    // Private helper methods
    private List<PackageDto> convertPackagesToDtoList(List<Package> packages) {
        logger.debug("Converting {} packages to DTO list", packages.size());

        return packages.stream()
                .map(packageMapper::packageToPackageDto)
                .collect(Collectors.toList());
    }

    private PackageDto transformVoltPackageToDto(PackageRepository.VoltPackageInfo voltPackageInfo) {
        logger.debug("Transforming VoltPackage to PackageDto");

        return packageMapper.voltPackageToPackageDto(voltPackageInfo);
    }
}