package com.i2i.intern.kotam.aom.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class VoltDBManager {

    private static final Logger logger = LoggerFactory.getLogger(VoltDBManager.class);

    // VoltDB modülü hazır olana kadar mock metodlar
    public PackageInfo getPackageInfoByPackageId(Integer packageId) {
        logger.debug("Getting package info for package ID: {}", packageId);

        // Mock data - VoltDB modülü hazır olduğunda gerçek implementation gelecek
        return new PackageInfo(30, 100, 50, 1024); // 30 gün, 100 dk, 50 SMS, 1024 MB
    }

    public void insertBalance(int balanceId, Integer customerId, Integer packageId,
                              int amountMinutes, int amountSms, int amountData,
                              Timestamp sDate, Timestamp eDate) {
        logger.debug("Inserting balance to VoltDB - BalanceId: {}, CustomerId: {}", balanceId, customerId);

        // VoltDB modülü hazır olduğunda gerçek implementation gelecek
        // voltdbClient.callProcedure("InsertBalance", balanceId, customerId, packageId, ...);

        logger.debug("Balance inserted successfully to VoltDB");
    }

    public int getMaxBalanceId() {
        logger.debug("Getting max balance ID from VoltDB");

        // Mock data - VoltDB modülü hazır olduğunda gerçek implementation gelecek
        return 1000; // Mock değer
    }

    // Inner class for package info
    public static class PackageInfo {
        private final int period;
        private final int amountMinutes;
        private final int amountSms;
        private final int amountData;

        public PackageInfo(int period, int amountMinutes, int amountSms, int amountData) {
            this.period = period;
            this.amountMinutes = amountMinutes;
            this.amountSms = amountSms;
            this.amountData = amountData;
        }

        public int period() { return period; }
        public int amountMinutes() { return amountMinutes; }
        public int amountSms() { return amountSms; }
        public int amountData() { return amountData; }
    }
}
