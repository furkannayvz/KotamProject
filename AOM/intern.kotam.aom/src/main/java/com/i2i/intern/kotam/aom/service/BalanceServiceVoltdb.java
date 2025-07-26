package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.helper.VoltDbOperator;
import com.i2i.intern.kotam.aom.model.Balance;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
public class BalanceServiceVoltdb  {

    private final VoltDbOperator voltDbOperator;

    public BalanceServiceVoltdb(VoltDbOperator voltDbOperator) {
        this.voltDbOperator = voltDbOperator;
    }

    public Optional<Balance> getBalanceByMsisdn(String msisdn) {
        try {
            JSONObject json = voltDbOperator.getBalanceInfoByMsisdn(msisdn);
            Balance balance = Balance.fromJson(json); // JSON → Balance dönüştürme
            return Optional.of(balance);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean insertBalance(Balance balance) {
        System.out.println("insertBalance METODU ÇAĞRILDI");

        try {
            //String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(balance.getsDate());

            // Balance nesnesini JSON’a dönüştür
            JSONObject balanceJson = new JSONObject();

            System.out.println("1");
            balanceJson.put("BALANCE_ID", balance.getBalanceId());

            System.out.println("2");
            balanceJson.put("MSISDN", balance.getMsisdn());

            System.out.println("3");
            //balanceJson.put("PACKAGE_ID", balance.getPackageId());
            if (balance.getPackageEntity() != null && balance.getPackageEntity().getId() != null) {
                balanceJson.put("PACKAGE_ID", balance.getPackageEntity().getId());
            } else {
                System.err.println("PACKAGE_ID null geldi, kontrol et!");
            }

            System.out.println("4");
            balanceJson.put("BAL_LEFT_MINUTES", balance.getLeftMinutes());

            System.out.println("5");
            balanceJson.put("BAL_LEFT_SMS", balance.getLeftSms());

            System.out.println("6");
            balanceJson.put("BAL_LEFT_DATA", balance.getLeftData());

            //System.out.println("6");
            //balanceJson.put("SDATE", formattedDate);

            System.out.println("json tamamlandi " + balanceJson.toString());

            voltDbOperator.insertBalance(balanceJson);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Long> getMaxBalanceId() {
        try {
            int id = voltDbOperator.getMaxBalanceId();
            return Optional.of((long) id);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
