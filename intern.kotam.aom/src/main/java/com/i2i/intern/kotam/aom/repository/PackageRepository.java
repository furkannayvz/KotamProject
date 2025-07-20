package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.dto.PackageDetails;
import com.i2i.intern.kotam.aom.exception.DataNotFoundException;
import com.i2i.intern.kotam.aom.helper.DatabaseConnectionManager;
import com.i2i.intern.kotam.aom.helper.VoltdbOperator;
import com.i2i.intern.kotam.aom.model.Package;
import com.i2i.intern.kotam.aom.request.PackageActivationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.voltdb.client.ProcCallException;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PackageRepository {

    private final DatabaseConnectionManager dbManager;
    private final VoltdbOperator voltdbOperator;
    private final Logger logger = LoggerFactory.getLogger(PackageRepository.class);

    public PackageRepository(DatabaseConnectionManager dbManager, VoltdbOperator voltdbOperator) {
        this.dbManager = dbManager;
        this.voltdbOperator = voltdbOperator;
    }

    // Tüm paketleri listeleme (farklı metod ismi)
    public List<Package> retrieveAllPackagesFromOracle() throws SQLException, ClassNotFoundException {
        logger.info("Retrieving all packages from Oracle database");

        Connection connection = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Package> packages = new ArrayList<>();

        try {
            connection = dbManager.getOracleConnection();

            // Farklı stored procedure ismi
            stmt = buildAllPackagesQuery(connection);
            rs = executeAllPackagesQuery(stmt);

            packages = constructPackageListFromResultSet(rs);

            logger.info("Successfully retrieved {} packages from Oracle", packages.size());
            return packages;

        } catch (Exception e) {
            logger.error("Error retrieving packages from Oracle database", e);
            throw new RuntimeException("Failed to retrieve packages: " + e.getMessage());
        } finally {
            cleanupDatabaseResources(rs, stmt, connection);
        }
    }

    // MSISDN ile kullanıcı paketi alma (farklı metod ismi)
    public VoltPackageInfo fetchUserPackageByMsisdn(String msisdn) throws IOException, ProcCallException {
        logger.info("Fetching user package from VoltDB for MSISDN: {}", msisdn);

        try {
            // VoltDB'den paket bilgisi al
            VoltPackageInfo packageInfo = voltdbOperator.retrievePackageByMsisdn(msisdn);

            if (packageInfo == null) {
                logger.warn("No package found for MSISDN: {}", msisdn);
                throw new DataNotFoundException("Package not found for user: " + msisdn);
            }

            logger.info("Successfully fetched package for MSISDN: {}", msisdn);
            return packageInfo;

        } catch (Exception e) {
            logger.error("Error fetching user package for MSISDN: {}", msisdn, e);
            throw new RuntimeException("Failed to fetch user package: " + e.getMessage());
        }
    }

    // Paket detaylarını alma - PackageDetails döndüren versiyon
    public Optional<PackageDetails> searchPackageDetailsByName(String packageName) throws SQLException, ClassNotFoundException {
        logger.info("Searching package details for package: {}", packageName);

        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = dbManager.getOracleConnection();

            // Farklı stored procedure ismi
            stmt = buildPackageDetailsQuery(connection, packageName);
            PackageDetails packageDetails = executePackageDetailsQuery(stmt, packageName);

            if (packageDetails == null) {
                logger.warn("Package details not found for: {}", packageName);
                return Optional.empty();
            }

            logger.info("Package details found successfully for: {}", packageName);
            return Optional.of(packageDetails);

        } catch (Exception e) {
            logger.error("Error searching package details for: {}", packageName, e);
            throw new RuntimeException("Failed to search package details: " + e.getMessage());
        } finally {
            cleanupDatabaseResources(null, stmt, connection);
        }
    }

    // Paket aktivasyonu (ek özellik)
    public ResponseEntity<String> activatePackageForUser(PackageActivationRequest request) throws SQLException, ClassNotFoundException {
        logger.info("Activating package {} for MSISDN: {}", request.packageId(), request.msisdn());

        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = dbManager.getOracleConnection();

            // Paket varlığını kontrol et
            if (!verifyPackageExists(connection, request.packageId())) {
                logger.warn("Package not found with ID: {}", request.packageId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Package not found");
            }

            // Paket aktivasyonu
            stmt = buildPackageActivationQuery(connection, request);
            executePackageActivationQuery(stmt);

            logger.info("Package activated successfully for MSISDN: {}", request.msisdn());
            return ResponseEntity.ok("Package activated successfully");

        } catch (Exception e) {
            logger.error("Error activating package for MSISDN: {}", request.msisdn(), e);
            throw new RuntimeException("Failed to activate package: " + e.getMessage());
        } finally {
            cleanupDatabaseResources(null, stmt, connection);
        }
    }


    // Private helper methods - farklı isimler ve yapılar
    private CallableStatement buildAllPackagesQuery(Connection connection) throws SQLException {
        logger.debug("Building all packages query");

        CallableStatement stmt = connection.prepareCall("{call SELECT_ALL_PACKAGES(?)}"); // eski: RETRIEVE_ALL_PACKAGES
        stmt.registerOutParameter(1, Types.REF_CURSOR);

        return stmt;
    }


    private ResultSet executeAllPackagesQuery(CallableStatement stmt) throws SQLException {
        logger.debug("Executing all packages query");
        stmt.execute();
        return (ResultSet) stmt.getObject(1);
    }

    private List<Package> constructPackageListFromResultSet(ResultSet rs) throws SQLException {
        logger.debug("Constructing package list from result set");

        List<Package> packages = new ArrayList<>();

        while (rs.next()) {
            Package packageModel = buildPackageFromResultSetRow(rs);
            packages.add(packageModel);
        }

        return packages;
    }

    private CallableStatement buildPackageDetailsQuery(Connection connection, String packageName) throws SQLException {
        logger.debug("Building package details query for: {}", packageName);

        CallableStatement stmt = connection.prepareCall("{call SELECT_PACKAGE_DETAILS_BY_NAME(?, ?, ?, ?)}");
        stmt.setString(1, packageName);
        stmt.registerOutParameter(2, Types.INTEGER);
        stmt.registerOutParameter(3, Types.INTEGER);
        stmt.registerOutParameter(4, Types.INTEGER);

        return stmt;
    }

    private PackageDetails executePackageDetailsQuery(CallableStatement stmt, String packageName) throws SQLException {
        logger.debug("Executing package details query");
        stmt.execute();

        int amountMinutes = stmt.getInt(2);
        int amountSms = stmt.getInt(3);
        int amountData = stmt.getInt(4);

        // Paket bulunamadı kontrolü
        if (amountMinutes == 0 && amountSms == 0 && amountData == 0) {
            logger.warn("Package details not found for: {}", packageName);
            return null;
        }

        return constructPackageDetails(packageName, amountMinutes, amountSms, amountData);
    }

    private CallableStatement buildPackageActivationQuery(Connection connection, PackageActivationRequest request) throws SQLException {
        logger.debug("Building INSERT_BALANCE_TO_CUSTOMER procedure call");

        CallableStatement stmt = connection.prepareCall("{call INSERT_BALANCE_TO_CUSTOMER(?, ?, ?, ?, ?, ?)}");
        stmt.setString(1, request.msisdn());
        stmt.setInt(2, request.packageId());
        stmt.setInt(3, request.leftMinutes());
        stmt.setInt(4, request.leftSms());
        stmt.setInt(5, request.leftData());
        stmt.setDate(6, Date.valueOf(LocalDate.now())); // Şu anki tarihi veriyoruz

        return stmt;
    }


    private void executePackageActivationQuery(CallableStatement stmt) throws SQLException {
        logger.debug("Executing package activation query");
        stmt.execute();
    }






    private boolean verifyPackageExists(Connection connection, Integer packageId) throws SQLException {
        logger.debug("Verifying package existence for ID: {}", packageId);

        try (CallableStatement stmt = connection.prepareCall("{call CHECK_PACKAGE_EXISTS(?, ?)}")){
            stmt.setInt(1, packageId);
            stmt.registerOutParameter(2, Types.INTEGER);

            stmt.execute();

            int count = stmt.getInt(2);
            return count > 0;
        }
    }

    private Package buildPackageFromResultSetRow(ResultSet rs) throws SQLException {
        logger.debug("Building package from result set row");

        return Package.builder()
                .packageId(rs.getInt("PACKAGE_ID"))
                .packageName(rs.getString("PACKAGE_NAME"))
                .amountMinutes(rs.getInt("AMOUNT_MINUTES"))
                .amountSMS(0) // Prosedürde SMS dönmediği için varsayılan 0 verilebilir
                .amountData(rs.getInt("AMOUNT_DATA"))
                .price(rs.getDouble("PRICE"))
                .period(0) // Eğer prosedür "PKG_PERIOD" dönmüyorsa varsayılan verebilirsin
                .build();
    }


    // PackageInfo yerine PackageDetails döndüren metod
    private PackageDetails constructPackageDetails(String packageName, int amountMinutes, int amountSms, int amountData) {
        logger.debug("Constructing package details object");

        return PackageDetails.builder()
                .packageName(packageName)
                .amountMinutes(amountMinutes)
                .amountSms(amountSms)
                .amountData(amountData)
                .build();
    }

    private void cleanupDatabaseResources(AutoCloseable... resources) {
        logger.debug("Cleaning up database resources");

        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    logger.warn("Error cleaning up database resource", e);
                }
            }
        }
    }

    // VoltDB operations wrapper methods
    public static class VoltPackageInfo {
        private final String packageName;
        private final int amountMinutes;
        private final int amountSms;
        private final int amountData;
        private final double price;

        public VoltPackageInfo(String packageName, int amountMinutes, int amountSms, int amountData, double price) {
            this.packageName = packageName;
            this.amountMinutes = amountMinutes;
            this.amountSms = amountSms;
            this.amountData = amountData;
            this.price = price;
        }

        // Getters
        public String getPackageName() { return packageName; }
        public int getAmountMinutes() { return amountMinutes; }
        public int getAmountSms() { return amountSms; }
        public int getAmountData() { return amountData; }
        public double getPrice() { return price; }
    }
}