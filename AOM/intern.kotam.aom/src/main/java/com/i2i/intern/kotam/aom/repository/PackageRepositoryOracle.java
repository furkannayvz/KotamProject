
package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.model.PackageEntity;

import java.util.List;
import java.util.Optional;

public interface PackageRepositoryOracle {

    List<PackageEntity> getAllPackages();

    Optional<PackageEntity> getPackageDetailsById(Long id);

    Optional<PackageEntity> getPackageDetailsByName(String name);

    Optional<PackageEntity> getPackageDetailsByIdCursor(Long id);

    Optional<Long> getPackageIdByName(String name);
}


