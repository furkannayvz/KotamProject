package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.helper.VoltdbOperator;
import com.i2i.intern.kotam.aom.repository.BalanceRepository;
import com.i2i.intern.kotam.aom.request.BalanceUpdateRequest;
import com.i2i.intern.kotam.aom.request.CreateBalanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.voltdb.client.ProcCallException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Service
public class BalanceService {

    private static final Logger logger = LoggerFactory.getLogger(BalanceService.class);

    private final VoltdbOperator voltdbOperator;
    private final BalanceRepository balanceRepository;

    public BalanceService(VoltdbOperator voltdbOperator, BalanceRepository balanceRepository) {
        this.voltdbOperator = voltdbOperator;
        this.balanceRepository = balanceRepository;
    }

    /**
     * Müşteri bakiyesini MSISDN ile getir
     * @param msisdn Müşteri telefon numarası
     * @return VoltCustomerBalance
     * @throws IOException
     * @throws InterruptedException
     * @throws ProcCallException
     */
    public VoltCustomerBalance fetchCustomerBalanceByMsisdn(String msisdn)
            throws IOException, InterruptedException, ProcCallException {

        logger.info("Fetching customer balance for MSISDN: {}", msisdn);

        try {
            VoltCustomerBalance customerBalance = voltdbOperator.getRemainingCustomerBalanceByMsisdn(msisdn);

            if (customerBalance == null) {
                logger.warn("No balance found for MSISDN: {}", msisdn);
                throw new RuntimeException("Balance not found for MSISDN: " + msisdn);
            }

            logger.info("Successfully fetched balance for MSISDN: {}", msisdn);
            return customerBalance;

        } catch (Exception e) {
            logger.error("Error fetching customer balance for MSISDN: {}", msisdn, e);
            throw new RuntimeException("Failed to fetch customer balance: " + e.getMessage(), e);
        }
    }

    /**
     * Oracle'da müşteri bakiyesini getir
     * @param msisdn Müşteri telefon numarası
     * @return Balance bilgileri
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Map<String, Object> retrieveBalanceFromOracle(String msisdn)
            throws SQLException, ClassNotFoundException {

        logger.info("Retrieving balance from Oracle for MSISDN: {}", msisdn);

        try {
            ResponseEntity<Map<String, Object>> response = balanceRepository.getBalanceFromOracle(msisdn);

            if (response.getBody() == null || response.getBody().isEmpty()) {
                logger.warn("No balance data found in Oracle for MSISDN: {}", msisdn);
                throw new RuntimeException("Balance data not found in Oracle for MSISDN: " + msisdn);
            }

            logger.info("Successfully retrieved balance from Oracle for MSISDN: {}", msisdn);
            return response.getBody();

        } catch (Exception e) {
            logger.error("Error retrieving balance from Oracle for MSISDN: {}", msisdn, e);
            throw new RuntimeException("Failed to retrieve balance from Oracle: " + e.getMessage(), e);
        }
    }

    /**
     * Yeni bakiye oluştur
     * @param request Balance creation request
     * @return Response message
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String createCustomerBalance(CreateBalanceRequest request)
            throws SQLException, ClassNotFoundException {

        logger.info("Creating balance for customer: {}", request.customerId());

        try {
            ResponseEntity<String> response = balanceRepository.createOracleBalance(request);

            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.error("Failed to create balance for customer: {}", request.customerId());
                throw new RuntimeException("Balance creation failed for customer: " + request.customerId());
            }

            logger.info("Successfully created balance for customer: {}", request.customerId());
            return response.getBody();

        } catch (Exception e) {
            logger.error("Error creating balance for customer: {}", request.customerId(), e);
            throw new RuntimeException("Failed to create balance: " + e.getMessage(), e);
        }
    }

    /**
     * Bakiye güncelle
     * @param request Balance update request
     * @return Response message
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String updateCustomerBalance(BalanceUpdateRequest request)
            throws SQLException, ClassNotFoundException {

        logger.info("Updating balance for MSISDN: {}", request.msisdn());

        try {
            ResponseEntity<String> response = balanceRepository.updateBalanceInOracle(request);

            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.error("Failed to update balance for MSISDN: {}", request.msisdn());
                throw new RuntimeException("Balance update failed for MSISDN: " + request.msisdn());
            }

            logger.info("Successfully updated balance for MSISDN: {}", request.msisdn());
            return response.getBody();

        } catch (Exception e) {
            logger.error("Error updating balance for MSISDN: {}", request.msisdn(), e);
            throw new RuntimeException("Failed to update balance: " + e.getMessage(), e);
        }
    }

    // VoltCustomerBalance inner class (VoltDB modülü hazır olana kadar)
    public static class VoltCustomerBalance {
        private final String msisdn;
        private final int balanceLevelMinutes;
        private final int balanceLevelSMS;
        private final int balanceLevelData;
        private final int balanceLevelMoney;

        public VoltCustomerBalance(String msisdn, int balanceLevelMinutes, int balanceLevelSMS,
                                   int balanceLevelData, int balanceLevelMoney) {
            this.msisdn = msisdn;
            this.balanceLevelMinutes = balanceLevelMinutes;
            this.balanceLevelSMS = balanceLevelSMS;
            this.balanceLevelData = balanceLevelData;
            this.balanceLevelMoney = balanceLevelMoney;
        }

        // Getters
        public String getMsisdn() { return msisdn; }
        public int getBalanceLevelMinutes() { return balanceLevelMinutes; }
        public int getBalanceLevelSMS() { return balanceLevelSMS; }
        public int getBalanceLevelData() { return balanceLevelData; }
        public int getBalanceLevelMoney() { return balanceLevelMoney; }
    }
}