/*
package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.service.VoltDBManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository("customerRepositoryVoltdb")
public class CustomerRepositoryVoltdb implements CustomerRepositoryVoltdbBase {

    private final VoltDBManager voltDBManager;

    @Autowired
    public CustomerRepositoryVoltdb(VoltDBManager voltDBManager) {
        this.voltDBManager = voltDBManager;
    }

    @Override
    public boolean insertCustomer(Customer customer) {
        try {
            JSONObject response = voltDBManager.executeProcedure(
                    "INSERT_NEW_CUSTOMER",
                    customer.getMsisdn(),
                    customer.getName(),
                    customer.getSurname(),
                    customer.getEmail(),
                    customer.getPassword(),
                    customer.getsDate(),
                    customer.getNationalId()
            );
            return response.has("status") && response.getInt("status") == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Customer> getCustomerByMsisdn(String msisdn) {
        try {
            JSONObject response = voltDBManager.executeProcedure("GET_CUSTOMER_INFO_BY_MSISDN", msisdn);

            if (response.has("status") && response.getInt("status") == 1) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    JSONArray data = results.getJSONObject(0).getJSONArray("data");

                    if (data.length() > 0) {
                        JSONArray row = data.getJSONArray(0);

                        Customer customer = new Customer();
                        customer.setMsisdn(row.getString(0));
                        customer.setName(row.getString(1));
                        customer.setSurname(row.getString(2));
                        customer.setEmail(row.getString(3));
                        customer.setPassword(row.getString(4));
                        if (!row.isNull(5)) {
                            customer.setsDate(new Timestamp(row.getLong(5)));
                        }
                        customer.setNationalId(row.getString(6));

                        return Optional.of(customer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean updatePassword(String email, String nationalId, String newPassword) {
        try {
            JSONObject response = voltDBManager.executeProcedure(
                    "UPDATE_CUSTOMER_PASSWORD",
                    newPassword,
                    email,
                    nationalId
            );
            return response.has("status") && response.getInt("status") == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkCustomerExists(String email, String nationalId) {
        try {
            JSONObject response = voltDBManager.executeProcedure(
                    "CHECK_CUSTOMER_EXISTS_BY_MAIL_AND_NATIONAL_ID",
                    email,
                    nationalId
            );

            if (response.has("status") && response.getInt("status") == 1) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    long count = results.getJSONObject(0).getJSONArray("data").getJSONArray(0).getLong(0);
                    return count > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<String> getPasswordByMsisdn(String msisdn) {
        try {
            JSONObject response = voltDBManager.executeProcedure("GET_CUSTOMER_PASSWORD_BY_MSISDN", msisdn);

            if (response.has("status") && response.getInt("status") == 1) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    String password = results.getJSONObject(0).getJSONArray("data").getJSONArray(0).getString(0);
                    return Optional.of(password);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getEmailByMsisdn(String msisdn) {
        try {
            JSONObject response = voltDBManager.executeProcedure("GET_CUSTOMER_EMAIL_BY_MSISDN", msisdn);

            if (response.has("status") && response.getInt("status") == 1) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    String email = results.getJSONObject(0).getJSONArray("data").getJSONArray(0).getString(0);
                    return Optional.of(email);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getMaxMsisdn() {
        try {
            JSONObject response = voltDBManager.executeProcedure("GET_MAX_CUSTOMER_MSISDN");

            if (response.has("status") && response.getInt("status") == 1) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    String maxMsisdn = results.getJSONObject(0).getJSONArray("data").getJSONArray(0).getString(0);
                    return Optional.of(maxMsisdn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

 */
