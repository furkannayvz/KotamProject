package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.model.Balance;
import com.i2i.intern.kotam.aom.model.PackageEntity;
import com.i2i.intern.kotam.aom.service.VoltDBManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository("balanceRepositoryVoltdb")

public class BalanceRepositoryVoltdb implements BalanceRepositoryVoltdbBase {

    private final VoltDBManager voltDBManager;
    private final PackageRepository packageRepository;

    @Autowired
    public BalanceRepositoryVoltdb(VoltDBManager voltDBManager, PackageRepository packageRepository) {
        this.voltDBManager = voltDBManager;
        this.packageRepository = packageRepository;
    }

    @Override
    public Optional<Balance> getBalanceByMsisdn(String msisdn) {
        try {
            JSONObject response = voltDBManager.executeProcedure("GET_BALANCE_INFO_BY_MSISDN", msisdn);

            if (response.has("status") && response.getInt("status") == 1) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    JSONArray row = results.getJSONObject(0).getJSONArray("data").getJSONArray(0);

                    Balance balance = new Balance();
                    balance.setBalanceId(row.getLong(0));
                    balance.setMsisdn(row.getString(1));

                    Long packageId = row.getLong(2);
                    balance.setPackageId(packageId); // bu zaten var

                    balance.setLeftMinutes(row.getLong(3));
                    balance.setLeftSms(row.getLong(4));
                    balance.setLeftData(row.getLong(5));

                    if (!row.isNull(6)) {
                        balance.setsDate(new Timestamp(row.getLong(6)));
                    }

                    // PackageEntity'yi balance'a iliştir
                    Optional<PackageEntity> optionalPackage = packageRepository.getPackageDetailsById(packageId);

                    optionalPackage.ifPresent(balance::setPackageEntity);
                    System.out.println("Paket ID: " + packageId + ", gelen optionalPackage: " + optionalPackage);

                    return Optional.of(balance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public boolean insertBalance(Balance balance) {
        try {
            JSONObject response = voltDBManager.executeProcedure(
                    "INSERT_BALANCE",
                    balance.getBalanceId(),
                    balance.getMsisdn(),
                    balance.getPackageId(),
                    balance.getLeftMinutes(),
                    balance.getLeftSms(),
                    balance.getLeftData()
                    //balance.getsDate()
            );
            return response.has("status") && response.getInt("status") == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateMinutesByMsisdn(String msisdn, Long minutes) {
        try {
            JSONObject response = voltDBManager.executeProcedure("UPDATE_BALANCE_MINUTES_BY_MSISDN", minutes, msisdn);
            return response.has("status") && response.getInt("status") == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSmsByMsisdn(String msisdn, Long sms) {
        try {
            JSONObject response = voltDBManager.executeProcedure("UPDATE_BALANCE_SMS_BY_MSISDN", sms, msisdn);
            return response.has("status") && response.getInt("status") == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateDataByMsisdn(String msisdn, Long data) {
        try {
            JSONObject response = voltDBManager.executeProcedure("UPDATE_BALANCE_DATA_BY_MSISDN", data, msisdn);
            return response.has("status") && response.getInt("status") == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Long> getMaxBalanceId() {
        try {
            JSONObject response = voltDBManager.executeProcedure("GET_MAX_BALANCE_ID");

            if (response.has("status") && response.getInt("status") == 1) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    Long maxId = results.getJSONObject(0).getJSONArray("data").getJSONArray(0).getLong(0);
                    return Optional.of(maxId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
