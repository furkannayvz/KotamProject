package com.i2i.intern.kotam.aom.service;

// VoltDBManager sınıfı, bir VoltDB veritabanına REST API üzerinden erişim sağlayan servis katmanıdır

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Base64;

@Service
public class VoltDBManager {

    @Value("${voltdb.host:34.142.55.240}")
    private String voltdbHost;

    @Value("${voltdb.port:8081}")
    private int voltdbPort;

    @Value("${voltdb.username:}")
    private String voltdbUsername;

    @Value("${voltdb.password:}")
    private String voltdbPassword;

    private String getBaseUrl() {
        return String.format("http://%s:%d", voltdbHost, voltdbPort);
    }

    private String getAuthHeader() {
        if (voltdbUsername != null && !voltdbUsername.trim().isEmpty()) {
            String auth = voltdbUsername + ":" + voltdbPassword;
            return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
        }
        return null;
    }

    /**
     * Generic method to execute VoltDB stored procedures via REST API
     */
    public JSONObject executeProcedure(String procedureName, Object... parameters) throws Exception {
        String urlString = getBaseUrl() + "/api/1.0/";

        JSONObject requestBody = new JSONObject();
        requestBody.put("Procedure", procedureName);

        if (parameters.length > 0) {
            JSONArray params = new JSONArray();
            for (Object param : parameters) {
                params.put(param);
            }
            requestBody.put("Parameters", params);
        }

        return makeHttpRequest("POST", urlString, requestBody.toString());
    }

    /**
     * Generic HTTP request method
     */
    private JSONObject makeHttpRequest(String method, String urlString, String requestBody) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        // Add authentication if available
        String authHeader = getAuthHeader();
        if (authHeader != null) {
            conn.setRequestProperty("Authorization", authHeader);
        }

        // Send request body if provided
        if (requestBody != null && !requestBody.isEmpty()) {
            conn.setDoOutput(true);
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(requestBody);
                writer.flush();
            }
        }

        // Read response
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new JSONObject(response.toString());
    }


    // Customer-specific methods
    public JSONObject insertNewCustomer(String msisdn, String name, String surname, String email,
                                        String password, java.sql.Timestamp sDate, String nationalId) throws Exception {
        return executeProcedure("INSERT_NEW_CUSTOMER", msisdn, name, surname, email, password, sDate, nationalId);
    }

    public JSONObject getCustomerInfoByMsisdn(String msisdn) throws Exception {
        return executeProcedure("GET_CUSTOMER_INFO_BY_MSISDN", msisdn);
    }

    public JSONObject updateCustomerPassword(String newPassword, String email, String nationalId) throws Exception {
        return executeProcedure("UPDATE_CUSTOMER_PASSWORD", newPassword, email, nationalId);
    }

    public JSONObject checkCustomerExistsByMailAndNationalId(String email, String nationalId) throws Exception {
        return executeProcedure("CHECK_CUSTOMER_EXISTS_BY_MAIL_AND_NATIONAL_ID", email, nationalId);
    }

    public JSONObject getCustomerPasswordByMsisdn(String msisdn) throws Exception {
        return executeProcedure("GET_CUSTOMER_PASSWORD_BY_MSISDN", msisdn);
    }

    public JSONObject getCustomerEmailByMsisdn(String msisdn) throws Exception {
        return executeProcedure("GET_CUSTOMER_EMAIL_BY_MSISDN", msisdn);
    }

    public JSONObject getMaxCustomerMsisdn() throws Exception {
        return executeProcedure("GET_MAX_CUSTOMER_MSISDN");
    }

    // Balance-specific methods
    public JSONObject insertBalance(Long balanceId, String msisdn, Long packageId, Long leftMinutes, Long leftSms, Long leftData, Long sDate) throws Exception {
        return executeProcedure("INSERT_BALANCE", balanceId, msisdn, packageId, leftMinutes, leftSms, leftData, sDate);
    }

    public JSONObject getBalanceInfoByMsisdn(String msisdn) throws Exception {
        return executeProcedure("GET_BALANCE_INFO_BY_MSISDN", msisdn);
    }

    public JSONObject updateBalanceMinutesByMsisdn(Long newMinutes, String msisdn) throws Exception {
        return executeProcedure("UPDATE_BALANCE_MINUTES_BY_MSISDN", newMinutes, msisdn);
    }

    public JSONObject updateBalanceSmsByMsisdn(Long newSms, String msisdn) throws Exception {
        return executeProcedure("UPDATE_BALANCE_SMS_BY_MSISDN", newSms, msisdn);
    }

    public JSONObject updateBalanceDataByMsisdn(Long newData, String msisdn) throws Exception {
        return executeProcedure("UPDATE_BALANCE_DATA_BY_MSISDN", newData, msisdn);
    }

    public JSONObject getMaxBalanceId() throws Exception {
        return executeProcedure("GET_MAX_BALANCE_ID");
    }


    // Package-specific methods
    public JSONObject getPackageNameByMsisdn(String msisdn) throws Exception {
        return executeProcedure("GET_PACKAGE_NAME_BY_MSISDN", msisdn);
    }

    public JSONObject getPackageNameByPackageId(Long packageId) throws Exception {
        return executeProcedure("GET_PACKAGE_NAME_BY_PACKAGE_ID", packageId);
    }

    public JSONObject getPackageInfoByPackageId(Long packageId) throws Exception {
        return executeProcedure("GET_PACKAGE_INFO_BY_PACKAGE_ID", packageId);
    }

    public JSONObject getPackageInfoByMsisdn(String msisdn) throws Exception {
        return executeProcedure("GET_PACKAGE_INFO_BY_MSISDN", msisdn);
    }

    // Additional package methods (for base interface compatibility)
    public JSONObject insertPackage(String packageName, Long amountMinutes, Long amountSms,
                                    Long amountData, Double price, Integer period) throws Exception {
        return executeProcedure("INSERT_PACKAGE", packageName, amountMinutes, amountSms, amountData, price, period);
    }

    public JSONObject updatePackage(Long packageId, String packageName, Long amountMinutes,
                                    Long amountSms, Long amountData, Double price, Integer period) throws Exception {
        return executeProcedure("UPDATE_PACKAGE", packageId, packageName, amountMinutes, amountSms, amountData, price, period);
    }

    public JSONObject deletePackage(Long packageId) throws Exception {
        return executeProcedure("DELETE_PACKAGE", packageId);
    }

    public JSONObject getAllPackages() throws Exception {
        return executeProcedure("GET_ALL_PACKAGES");
    }


    // System procedures
    public JSONObject getSystemInformation() throws Exception {
        return executeProcedure("@SystemInformation");
    }

    public JSONObject getStatistics(String statisticName) throws Exception {
        return executeProcedure("@Statistics", statisticName);
    }

    /**
     * Test connection to VoltDB
     */
    public boolean testConnection() {
        try {
            // Simple system procedure to test connection
            JSONObject response = executeProcedure("@SystemInformation");
            return response != null && response.has("status") && response.getInt("status") == 1;
        } catch (Exception e) {
            System.err.println("VoltDB connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Execute custom SQL query (if VoltDB supports it via REST)
     */
    public JSONObject executeQuery(String sql) throws Exception {
        String urlString = getBaseUrl() + "/api/1.0/";

        JSONObject requestBody = new JSONObject();
        requestBody.put("Procedure", "@AdHoc");

        JSONArray params = new JSONArray();
        params.put(sql);
        requestBody.put("Parameters", params);

        return makeHttpRequest("POST", urlString, requestBody.toString());
    }

    /**
     * Get current database configuration
     */
    public String getDatabaseInfo() {
        return String.format("VoltDB REST API - Host: %s:%d, Auth: %s",
                voltdbHost, voltdbPort, (voltdbUsername != null && !voltdbUsername.isEmpty() ? "Enabled" : "Disabled"));
    }
}