package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.exception.DataNotFoundException;
import com.i2i.intern.kotam.aom.helper.DatabaseConnectionManager;
import com.i2i.intern.kotam.aom.helper.VoltDBManager;
import com.i2i.intern.kotam.aom.request.CreateBalanceRequest;
import com.i2i.intern.kotam.aom.request.BalanceUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BalanceRepository {

    private final DatabaseConnectionManager dbManager;
    private final VoltDBManager voltManager;
    private final Logger logger = LoggerFactory.getLogger(BalanceRepository.class);

    public BalanceRepository(DatabaseConnectionManager dbManager, VoltDBManager voltManager) {
        this.dbManager = dbManager;
        this.voltManager = voltManager;
    }

    public ResponseEntity<String> createOracleBalance(CreateBalanceRequest request) {
        logger.info("Starting Oracle balance creation for customer: {}", request.customerId());

        Connection connection = null;
        try {
            connection = dbManager.getOracleConnection();

            VoltDBManager.PackageInfo packageInfo = retrievePackageInfo(connection, request.packageId());
            Timestamp[] balancePeriod = generateBalancePeriod(packageInfo.period());

            executeBalanceInsertion(connection, request, packageInfo, balancePeriod);

            logger.info("Oracle balance created successfully for customer: {}", request.customerId());
            return ResponseEntity.status(HttpStatus.CREATED).body("Balance created successfully in Oracle");

        } catch (Exception e) {
            logger.error("Failed to create Oracle balance for customer: {}", request.customerId(), e);
            throw new RuntimeException("Oracle balance creation failed: " + e.getMessage());
        } finally {
            dbManager.closeConnection(connection);
        }
    }

    public ResponseEntity<String> createVoltBalance(CreateBalanceRequest request) {
        logger.info("Starting VoltDB balance creation for customer: {}", request.customerId());

        try {
            VoltDBManager.PackageInfo packageInfo = voltManager.getPackageInfoByPackageId(request.packageId());
            Timestamp[] balancePeriod = generateBalancePeriod(packageInfo.period());

            int newBalanceId = voltManager.getMaxBalanceId() + 1;

            voltManager.insertBalance(
                    newBalanceId,
                    request.customerId(),
                    request.packageId(),
                    packageInfo.amountMinutes(),
                    packageInfo.amountSms(),
                    packageInfo.amountData(),
                    balancePeriod[0],
                    balancePeriod[1]
            );

            logger.info("VoltDB balance created successfully for customer: {}", request.customerId());
            return ResponseEntity.status(HttpStatus.CREATED).body("Balance created successfully in VoltDB");

        } catch (Exception e) {
            logger.error("Failed to create VoltDB balance for customer: {}", request.customerId(), e);
            throw new RuntimeException("VoltDB balance creation failed: " + e.getMessage());
        }
    }

    // EKSİK METOD 1: Oracle'dan bakiye getirme
    public ResponseEntity<Map<String, Object>> getBalanceFromOracle(String msisdn) {
        logger.info("Getting balance from Oracle for MSISDN: {}", msisdn);

        Connection connection = null;
        try {
            connection = dbManager.getOracleConnection();

            try (CallableStatement stmt = connection.prepareCall("{call GET_BALANCE_BY_MSISDN(?, ?, ?, ?, ?)}")) {
                stmt.setString(1, msisdn);
                stmt.registerOutParameter(2, Types.INTEGER);
                stmt.registerOutParameter(3, Types.INTEGER);
                stmt.registerOutParameter(4, Types.INTEGER);
                stmt.registerOutParameter(5, Types.INTEGER);

                stmt.execute();

                Map<String, Object> balanceData = new HashMap<>();
                balanceData.put("msisdn", msisdn);
                balanceData.put("balanceLevelMinutes", stmt.getInt(2));
                balanceData.put("balanceLevelSMS", stmt.getInt(3));
                balanceData.put("balanceLevelData", stmt.getInt(4));
                balanceData.put("balanceLevelMoney", stmt.getInt(5));

                logger.info("Balance retrieved successfully from Oracle for MSISDN: {}", msisdn);
                return ResponseEntity.ok(balanceData);
            }

        } catch (Exception e) {
            logger.error("Failed to get balance from Oracle for MSISDN: {}", msisdn, e);
            throw new RuntimeException("Failed to get balance from Oracle: " + e.getMessage());
        } finally {
            dbManager.closeConnection(connection);
        }
    }

    // EKSİK METOD 2: Oracle'da bakiye güncelleme
    public ResponseEntity<String> updateBalanceInOracle(BalanceUpdateRequest request) {
        logger.info("Updating balance in Oracle for MSISDN: {}", request.msisdn());

        Connection connection = null;
        try {
            connection = dbManager.getOracleConnection();

            try (CallableStatement stmt = connection.prepareCall("{call UPDATE_BALANCE(?, ?, ?, ?)}")) {
                stmt.setString(1, request.msisdn());
                stmt.setString(2, request.balanceType());
                stmt.setInt(3, request.amount());
                stmt.setString(4, request.operationType());

                stmt.execute();

                logger.info("Balance updated successfully in Oracle for MSISDN: {}", request.msisdn());
                return ResponseEntity.ok("Balance updated successfully in Oracle");
            }

        } catch (Exception e) {
            logger.error("Failed to update balance in Oracle for MSISDN: {}", request.msisdn(), e);
            throw new RuntimeException("Failed to update balance in Oracle: " + e.getMessage());
        } finally {
            dbManager.closeConnection(connection);
        }
    }

    private VoltDBManager.PackageInfo retrievePackageInfo(Connection connection, Integer packageId) throws SQLException {
        logger.debug("Retrieving package information for ID: {}", packageId);

        try (CallableStatement stmt = connection.prepareCall("{call SELECT_PACKAGE_DETAILS_ID(?, ?, ?, ?, ?)}")) {
            stmt.setInt(1, packageId);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.registerOutParameter(5, Types.INTEGER);

            stmt.execute();

            int minutes = stmt.getInt(2);
            int data = stmt.getInt(3);
            int sms = stmt.getInt(4);
            int period = stmt.getInt(5);

            if (minutes == 0 && sms == 0 && data == 0 && period == 0) {
                throw new DataNotFoundException("Package with ID " + packageId + " not found in Oracle");
            }

            return new VoltDBManager.PackageInfo(period, minutes, sms, data);
        }
    }

    private void executeBalanceInsertion(Connection connection, CreateBalanceRequest request,
                                         VoltDBManager.PackageInfo packageInfo, Timestamp[] period) throws SQLException {
        logger.debug("Executing balance insertion for customer: {}", request.customerId());

        try (CallableStatement stmt = connection.prepareCall("{call INSERT_BALANCE_TO_CUSTOMER(?, ?, ?, ?, ?, ?, ?)}")) {
            stmt.setInt(1, request.customerId());
            stmt.setInt(2, request.packageId());
            stmt.setInt(3, packageInfo.amountMinutes());
            stmt.setInt(4, packageInfo.amountSms());
            stmt.setInt(5, packageInfo.amountData());
            stmt.setTimestamp(6, period[0]);
            stmt.setTimestamp(7, period[1]);

            stmt.execute();
            logger.debug("Balance insertion completed successfully");
        }
    }

    private Timestamp[] generateBalancePeriod(int periodInDays) {
        long currentTime = System.currentTimeMillis();
        long periodInMillis = periodInDays * 24L * 60L * 60L * 1000L;

        Timestamp startDate = new Timestamp(currentTime);
        Timestamp endDate = new Timestamp(currentTime + periodInMillis);

        return new Timestamp[]{startDate, endDate};
    }
}