package com.i2i.intern.kotam.aom.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CustomerPasswordEncoder implements PasswordEncoder {

    private static final Logger logger = LoggerFactory.getLogger(CustomerPasswordEncoder.class);
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;

    @Override
    public String encode(CharSequence rawPassword) {
        logger.debug("Encoding customer password");

        try {
            // Generate salt
            byte[] salt = generateSalt();

            // Hash password with salt
            String hashedPassword = hashPassword(rawPassword.toString(), salt);

            // Combine salt and hash
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String result = saltBase64 + ":" + hashedPassword;

            logger.debug("Customer password encoded successfully");
            return result;

        } catch (Exception e) {
            logger.error("Error encoding customer password", e);
            throw new RuntimeException("Password encoding failed", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        logger.debug("Verifying customer password");

        try {
            // Split salt and hash
            String[] parts = encodedPassword.split(":");
            if (parts.length != 2) {
                logger.warn("Invalid encoded password format");
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String storedHash = parts[1];

            // Hash the provided password with stored salt
            String hashedPassword = hashPassword(rawPassword.toString(), salt);

            // Compare hashes
            boolean isValid = storedHash.equals(hashedPassword);
            logger.debug("Customer password verification result: {}", isValid);

            return isValid;

        } catch (Exception e) {
            logger.error("Error verifying customer password", e);
            return false;
        }
    }

    public String encrypt(String password) {
        return encode(password);
    }

    private byte[] generateSalt() {
        logger.debug("Generating salt for password encoding");

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
}