package com.voltdb.service;

import com.voltdb.dto.BalanceDTO;
import com.voltdb.dto.CustomerDTO;
import com.voltdb.dto.PackageDTO;
import org.springframework.stereotype.Service;
import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;

@Service
public class BalanceService {

    private final Client voltClient;

    public BalanceService(Client voltClient){
        this.voltClient = voltClient;
    }

    public BalanceDTO getBalanceInfoByMsisdn(String msisdn)throws Exception{
        ClientResponse response = voltClient.callProcedure("GET_BALANCE_INFO_BY_MSISDN", msisdn);
        VoltTable vt = response.getResults()[0];

        if (!vt.advanceRow()) {
            return null;
        }

        BalanceDTO dto = new BalanceDTO();

        dto.BALANCE_ID = (int)vt.getLong("BALANCE_ID");
        dto.MSISDN = msisdn;
        dto.PACKAGE_ID = (int)vt.getLong("PACKAGE_ID");
        dto.BAL_LEFT_MINUTES = (int)vt.getLong("BAL_LEFT_MINUTES");
        dto.BAL_LEFT_SMS = (int)vt.getLong("BAL_LEFT_SMS");
        dto.BAL_LEFT_DATA = (int)vt.getLong("BAL_LEFT_DATA");
        dto.SDATE = vt.getTimestampAsSqlTimestamp("SDATE");

        return dto;
    }

    public void updateBalanceMinutesByMsisdn(int minutes,String msisdn)throws Exception{
        voltClient.callProcedure("UPDATE_BALANCE_MINUTES_BY_MSISDN",
                minutes,msisdn);
    }

    public void updateBalanceSmsByMsisdn(int sms,String msisdn)throws Exception{
        voltClient.callProcedure("UPDATE_BALANCE_SMS_BY_MSISDN",
                sms,msisdn);
    }
    public void updateBalanceDataByMsisdn(int data,String msisdn)throws Exception{
        voltClient.callProcedure("UPDATE_BALANCE_DATA_BY_MSISDN",
                data,msisdn);
    }

    public void insertBalance(BalanceDTO dto)throws Exception {

        voltClient.callProcedure("INSERT_BALANCE",
                dto.BALANCE_ID,
                dto.MSISDN,
                dto.PACKAGE_ID,
                dto.BAL_LEFT_MINUTES,
                dto.BAL_LEFT_SMS,
                dto.BAL_LEFT_DATA,
                dto.SDATE
        );
    }

    public int getMaxBalanceId() throws Exception{
        ClientResponse response = voltClient.callProcedure("GET_MAX_BALANCE_ID");
        VoltTable vt = response.getResults()[0];
        return (int) vt.getLong(0);
    }


    public String handleProcedureString(String procedure,String input) throws Exception{
        ClientResponse response = voltClient.callProcedure(procedure,
                input);
        VoltTable vt = response.getResults()[0];
        return vt.getString(0);
    }


    public String handleProcedureInt(String procedure,int input) throws Exception{
        ClientResponse response = voltClient.callProcedure(procedure,
                input);
        VoltTable vt = response.getResults()[0];
        return vt.getString(0);
    }






}
