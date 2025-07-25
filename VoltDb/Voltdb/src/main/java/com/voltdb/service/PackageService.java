package com.voltdb.service;

import com.voltdb.dto.PackageDTO;
import org.springframework.stereotype.Service;
import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;

@Service
public class PackageService {

    private final Client voltClient;

    public PackageService(Client voltClient){
        this.voltClient = voltClient;
    }

    public String getPackageNameByMsisdn(String msisdn) throws Exception{
        return handleProcedureString("GET_PACKAGE_NAME_BY_MSISDN",msisdn);
    }

    public String getPackageNameByPackageId(int packageId) throws Exception{
        return handleProcedureInt("GET_PACKAGE_NAME_BY_MSISDN",packageId);
    }

    public PackageDTO getPackageInfoById(int packageId) throws Exception{
        ClientResponse response = voltClient.callProcedure("GET_PACKAGE_INFO_BY_PACKAGE_ID", packageId);
        VoltTable vt = response.getResults()[0];

        if (!vt.advanceRow()) {
            return null; // müşteri bulunamadı
        }

        PackageDTO dto = new PackageDTO();

        dto.PACKAGE_ID = packageId;
        dto.PACKAGE_NAME = vt.getString("PACKAGE_NAME");
        dto.PRICE = vt.getDecimalAsBigDecimal("PRICE");
        dto.AMOUNT_MINUTES = (int) vt.getLong("AMOUNT_MINUTES");
        dto.AMOUNT_SMS = (int) vt.getLong("AMOUNT_SMS");
        dto.AMOUNT_DATA = (int) vt.getLong("AMOUNT_DATA");
        dto.PERIOD = (int) vt.getLong("PERIOD");
        return dto;
    }

    public PackageDTO getPackageInfoByMsisdn(String msisdn) throws Exception{
        ClientResponse response = voltClient.callProcedure("GET_PACKAGE_INFO_BY_MSISDN", msisdn);
        VoltTable vt = response.getResults()[0];

        if (!vt.advanceRow()) {
            return null;
        }

        PackageDTO dto = new PackageDTO();

        dto.PACKAGE_ID = (int) vt.getLong("PACKAGE_ID");
        dto.PACKAGE_NAME = vt.getString("PACKAGE_NAME");
        dto.PRICE = vt.getDecimalAsBigDecimal("PRICE");
        dto.AMOUNT_MINUTES = (int) vt.getLong("AMOUNT_MINUTES");
        dto.AMOUNT_SMS = (int) vt.getLong("AMOUNT_SMS");
        dto.AMOUNT_DATA = (int) vt.getLong("AMOUNT_DATA");
        dto.PERIOD = (int) vt.getLong("PERIOD");
        return dto;
    }


    public String handleProcedureString(String procedure,String input) throws Exception{
        ClientResponse response = voltClient.callProcedure(procedure,
                input);
        VoltTable vt = response.getResults()[0];

        if (!vt.advanceRow()) {
            return null; // müşteri bulunamadı
        }
        return vt.getString(0);
    }


    public String handleProcedureInt(String procedure,int input) throws Exception{
        ClientResponse response = voltClient.callProcedure(procedure,
                input);
        VoltTable vt = response.getResults()[0];

        if (!vt.advanceRow()) {
            return null; // müşteri bulunamadı
        }
        return vt.getString(0);
    }
}
