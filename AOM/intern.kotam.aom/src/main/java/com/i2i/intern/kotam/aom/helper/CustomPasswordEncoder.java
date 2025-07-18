package com.i2i.intern.kotam.aom.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CustomPasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder {

    private static final Logger logger = LoggerFactory.getLogger(CustomPasswordEncoder.class);
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;

    @Override
    public String encode(CharSequence rawPassword) {
        return encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return verify(rawPassword.toString(), encodedPassword);
    }

    public String encrypt(String password) {
        logger.debug("Encrypting password");

        try {
            // Generate salt
            byte[] salt = generateSalt();

            // Hash password with salt
            String hashedPassword = hashPassword(password, salt);

            // Combine salt and hash
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String result = saltBase64 + ":" + hashedPassword;

            logger.debug("Password encrypted successfully");
            return result;

        } catch (Exception e) {
            logger.error("Error encrypting password", e);
            throw new RuntimeException("Password encryption failed", e);
        }
    }

    public boolean verify(String password, String encryptedPassword) {
        logger.debug("Verifying password");

        try {
            // Split salt and hash
            String[] parts = encryptedPassword.split(":");
            if (parts.length != 2) {
                logger.warn("Invalid encrypted password format");
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String storedHash = parts[1];

            // Hash the provided password with stored salt
            String hashedPassword = hashPassword(password, salt);

            // Compare hashes
            boolean isValid = storedHash.equals(hashedPassword);
            logger.debug("Password verification result: {}", isValid);

            return isValid;

        } catch (Exception e) {
            logger.error("Error verifying password", e);
            return false;
        }
    }

    private byte[] generateSalt() {
        logger.debug("Generating salt");

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        return salt;
    }

    private String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        logger.debug("Hashing password with salt");

        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(salt);

        byte[] hashedPassword = md.digest(password.getBytes());

        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    public String generateRandomPassword(int length) {
        logger.debug("Generating random password with length: {}", length);

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        logger.debug("Random password generated successfully");
        return password.toString();
    }

    public boolean isPasswordStrong(String password) {
        logger.debug("Checking password strength");

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isWhitespace(c)) hasSpecial = true;
        }

        boolean isStrong = hasUpper && hasLower && hasDigit && hasSpecial;
        logger.debug("Password strength check result: {}", isStrong);

        return isStrong;
    }
}