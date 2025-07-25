package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.model.PackageEntity;
import com.i2i.intern.kotam.aom.repository.PackageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PackageServiceOracle {

    private final PackageRepository packageRepository;

    public PackageServiceOracle(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public Optional<PackageEntity> getPackageDetailsById(Long id) {
        // 🔍 Test için log ekliyoruz:
        System.out.println("Package ID: " + id);

        Optional<PackageEntity> optionalPackage = packageRepository.getPackageDetailsById(id);

        System.out.println("PackageEntity isPresent: " + optionalPackage.isPresent());

        return optionalPackage;
    }

    public Optional<PackageEntity> getPackageDetailsByName(String name) {
        return packageRepository.getPackageDetailsByName(name);
    }

    public Optional<Long> getPackageIdByName(String name) {
        return packageRepository.getPackageIdByName(name);
    }

    public List<PackageEntity> getAllPackages() {
        return packageRepository.getAllPackages();
    }
}
