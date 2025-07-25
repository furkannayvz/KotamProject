package com.i2i.intern.kotam.aom.helper;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

@Component
public class VoltDbOperator {
    private final String urlBase = "http://34.142.55.240:8081/";

    public JSONObject getBalanceInfoByMsisdn(String msisdn) throws IOException {
        URL url = new URL(urlBase + "balances/" + msisdn);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return new JSONObject(response.toString());
        } else {
            System.err.println("Balance bilgisi alınamadı. HTTP Kod: " + responseCode);
            return null;
        }
    }


    public void insertBalance(JSONObject balanceJson) {
        balanceJson.toString(4);
        try {
            JSONObject formattedJson = new JSONObject();

            formattedJson.put("BALANCE_ID", balanceJson.getInt("BALANCE_ID"));
            formattedJson.put("MSISDN", balanceJson.getString("MSISDN"));
            formattedJson.put("PACKAGE_ID", balanceJson.getInt("PACKAGE_ID"));
            formattedJson.put("BAL_LEFT_MINUTES", balanceJson.getInt("BAL_LEFT_MINUTES"));
            formattedJson.put("BAL_LEFT_SMS", balanceJson.getInt("BAL_LEFT_SMS"));
            formattedJson.put("BAL_LEFT_DATA", balanceJson.getInt("BAL_LEFT_DATA"));


            /*
            formattedJson.put("balanceId", balanceJson.getInt("BALANCE_ID"));
            formattedJson.put("msisdn", balanceJson.getString("MSISDN"));
            formattedJson.put("packageId", balanceJson.getInt("PACKAGE_ID"));
            formattedJson.put("leftMinutes", balanceJson.getInt("BAL_LEFT_MINUTES"));
            formattedJson.put("leftSms", balanceJson.getInt("BAL_LEFT_SMS"));
            formattedJson.put("leftData", balanceJson.getInt("BAL_LEFT_DATA"));
            //formattedJson.put("getsDate", balanceJson.getString("SDATE").replace(" ", "T")); */

            System.out.println("Gönderilen JSON:\n" + formattedJson.toString(4));

            URL url = new URL("http://34.142.55.240:8081/balances");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(formattedJson.toString().getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Balance Ekleme Yanıt Kodu: " + responseCode);

            InputStream stream = (responseCode >= 200 && responseCode < 300)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            balanceJson.toString();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String responseLine;
                StringBuilder response = new StringBuilder();
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Yanıt Mesajı: " + response);
            }

            System.out.println("bitirdim");

            conn.disconnect();
        } catch (Exception e) {
            System.err.println("Hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getMaxBalanceId() throws IOException {
        URL url = new URL(urlBase + "balances/max-Id");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }

        //  Yanıtı burada yazdırıyoruz:
        System.out.println(" VoltDB'den gelen yanıt: " + response);

        in.close();

        // Geçici olarak sadece dönelim:
        return Integer.parseInt(response.toString().trim());
    }
}
