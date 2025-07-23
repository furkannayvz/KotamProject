package com.i2i.intern.kotam.aom.service;


import com.i2i.intern.kotam.aom.model.Balance;
import com.i2i.intern.kotam.aom.model.PackageEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class PackageSelectionService {

    private final PackageServiceOracle packageServiceOracle;
    private final BalanceServiceVoltdb balanceServiceVoltdb;
    private final HazelcastService hazelcastService;

    public PackageSelectionService(PackageServiceOracle packageServiceOracle,
                                   BalanceServiceVoltdb balanceServiceVoltdb,
                                   HazelcastService hazelcastService) {
        this.packageServiceOracle = packageServiceOracle;
        this.balanceServiceVoltdb = balanceServiceVoltdb;
        this.hazelcastService = hazelcastService;
    }

    public boolean processPackageSelection(String msisdn, Long packageId) {
        // 1. Oracle'dan paketi al
        Optional<PackageEntity> optionalPackage = packageServiceOracle.getPackageDetailsById(packageId);
        if (optionalPackage.isEmpty()) {
            System.err.println("Paket bulunamadı. ID: " + packageId);
            return false;
        }

        PackageEntity selectedPackage = optionalPackage.get();

        // 2. Balance nesnesi oluştur
        Long newBalanceId = balanceServiceVoltdb.getMaxBalanceId().orElse(100L) + 1;

        Balance balance = new Balance();
        balance.setBalanceId(newBalanceId);
        balance.setMsisdn(msisdn);
        balance.setLeftData(selectedPackage.getDataQuota());
        balance.setLeftSms(selectedPackage.getSmsQuota());
        balance.setLeftMinutes(selectedPackage.getMinutesQuota());
        balance.setsDate(Timestamp.from(Instant.now()));
        balance.setPackageEntity(selectedPackage);

        // 3. VoltDB’ye yaz
        boolean voltSuccess = balanceServiceVoltdb.insertBalance(balance);
        if (!voltSuccess) {
            System.err.println("VoltDB'ye kayıt başarısız.");
            return false;
        }

        // 4. Hazelcast’e yaz
        String result = hazelcastService.sendMsisdnToHazelcast(msisdn, "active");
        System.out.println("Hazelcast sonucu: " + result);

        return true;
    }
}
