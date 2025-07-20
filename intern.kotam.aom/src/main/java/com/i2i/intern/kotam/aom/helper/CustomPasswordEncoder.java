package com.i2i.intern.kotam.aom.helper;


import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class CustomPasswordEncoder {

    /**
     * Basit SHA-256 şifreleme algoritması (örnek amaçlıdır)
     */
    public String encrypt(String rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(rawPassword.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password encryption failed", e);
        }
    }
}
