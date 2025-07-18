package com.voltdb.service;

import com.voltdb.dto.CustomerDTO;
import org.springframework.stereotype.Service;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;
import org.voltdb.VoltTable;

@Service
public class CustomerService {

    private final Client voltClient;

    public CustomerService(Client voltClient) {
        this.voltClient = voltClient;
    }

    public void insertCustomer(CustomerDTO dto) throws Exception {
        voltClient.callProcedure("INSERT_NEW_CUSTOMER",
                dto.msisdn,
                dto.name,
                dto.surname,
                dto.email,
                dto.password,
                dto.sdate,
                dto.national_id
        );
    }

    public String getMaxCustomerMsisdn() throws Exception{
        ClientResponse response = voltClient.callProcedure("GET_MAX_CUSTOMER_MSISDN");
        VoltTable vt = response.getResults()[0];
        return vt.getString(0);
    }

    public CustomerDTO getCustomerInfoByMsisdn(String msisdn) throws Exception {
        ClientResponse response = voltClient.callProcedure("GET_CUSTOMER_INFO_BY_MSISDN", msisdn);
        VoltTable vt = response.getResults()[0];


        if (!vt.advanceRow()) {
            return null; // müşteri bulunamadı
        }

        CustomerDTO dto = new CustomerDTO();
        dto.msisdn = vt.getString("MSISDN");
        dto.name = vt.getString("NAME");
        dto.surname= vt.getString("SURNAME");
        dto.email = vt.getString("EMAIL");
        dto.password = vt.getString("PASSWORD");
        dto.sdate = vt.getTimestampAsSqlTimestamp("SDATE");
        dto.national_id = vt.getString("NATIONAL_ID");

        return dto;
    }

    public void updateCustomerPassword(String password,String email,String national_id) throws Exception{
        voltClient.callProcedure("UPDATE_CUSTOMER_PASSWORD",
                password,
                email,
                national_id
        );
    }

    public int checkIfCustomerExists(String email,String national_id) throws Exception{
        ClientResponse response = voltClient.callProcedure("CHECK_CUSTOMER_EXISTS_BY_MAIL_AND_NATIONAL_ID",
                email,
                national_id);
        VoltTable vt = response.getResults()[0];
        return Integer.parseInt(vt.getString(0));
    }

    public String getCustomerPasswordByMsisdn(String msisdn) throws Exception{
        return handleProcedureString("GET_CUSTOMER_PASSWORD_BY_MSISDN",msisdn);
    }

    public String getCustomerEmailByMsisdn(String msisdn) throws Exception{
        return handleProcedureString("GET_CUSTOMER_EMAIL_BY_MSISDN",msisdn);
    }


    public String handleProcedureString(String procedure,String input) throws Exception{
        ClientResponse response = voltClient.callProcedure(procedure,
                input);
        VoltTable vt = response.getResults()[0];
        return vt.getString(0);
    }


}

