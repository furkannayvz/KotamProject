package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.helper.DatabaseConnectionManager;
import com.i2i.intern.kotam.aom.repository.CustomerRepository;
import com.i2i.intern.kotam.aom.request.ForgetPasswordRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.*;

@Service
public class ForgetPasswordService {

    private static final Logger logger = LoggerFactory.getLogger(ForgetPasswordService.class);

    private final CustomerRepository customerRepository;
    private final JavaMailSender mailSender;
    private final DatabaseConnectionManager dbManager;

    public ForgetPasswordService(CustomerRepository customerRepository,
                                 JavaMailSender mailSender,
                                 DatabaseConnectionManager dbManager) {
        this.customerRepository = customerRepository;
        this.mailSender = mailSender;
        this.dbManager = dbManager;
    }

    public ResponseEntity<String> processPasswordReset(ForgetPasswordRequest request) {
        try {
            logger.info("Processing password reset for email: {}", request.email());

            if (!customerRepository.checkCustomerExists(request.email(), request.TCNumber(), request.msisdn())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer does not exist");
            }

            String newPassword = createRandomPassword();

            try {
                dispatchPasswordEmail(request.email(), newPassword);
            } catch (SendFailedException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send email");
            } catch (MessagingException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while sending email");
            }

            try {
                customerRepository.updatePasswordInOracle(request.email(), request.TCNumber(), newPassword);
                customerRepository.updatePasswordInVoltDB(request.email(), request.TCNumber(), newPassword);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while resetting password");
            }

            int customerId = fetchCustomerIdFromDatabase(request.email(), request.TCNumber());
            try {
                insertNotificationLog("Password Reset", new Timestamp(System.currentTimeMillis()), customerId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while inserting notification logs");
            }

            logger.info("Password reset completed for email: {}", request.email());
            return ResponseEntity.ok("Please check your mail address.");

        } catch (Exception e) {
            logger.error("Exception occurred during password reset", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while resetting password");
        }
    }

    private void dispatchPasswordEmail(String email, String newPassword) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Your New Password");

        String body = "<p>Hello,</p>"
                + "<p>Your password has been reset. Here is your new password:</p>"
                + "<h3>" + newPassword + "</h3>"
                + "<p>Please keep it safe and secure.</p>"
                + "<p>Best regards,<br/><b>Kotam Team</b></p>";

        helper.setText(body, true);
        mailSender.send(message);
    }

    private int fetchCustomerIdFromDatabase(String email, String tcNumber) {
        int customerId = -1;
        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = dbManager.getOracleConnection();
            stmt = connection.prepareCall("{call SELECT_CUSTOMER_ID_BY_EMAIL_AND_TCNUMBER(?, ?, ?)}");
            stmt.setString(1, email);
            stmt.setString(2, tcNumber);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();
            customerId = stmt.getInt(3);
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Error while fetching customer ID", e);
        } finally {
            dbManager.closeResources(stmt, connection);
        }

        return customerId;
    }

    private void insertNotificationLog(String message, Timestamp notificationTime, int customerId) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = dbManager.getOracleConnection();
            String sql = "INSERT INTO NOTIFICATION_LOG (message, notification_time, customer_id) VALUES (?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, message);
            stmt.setTimestamp(2, notificationTime);
            stmt.setInt(3, customerId);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Error while inserting notification log", e);
        } finally {
            dbManager.closeResources(stmt, connection);
        }
    }

    private String createRandomPassword() {
        final String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        final String numbers = "0123456789";

        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder(15);

        password.append(upperCaseLetters.charAt(secureRandom.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(secureRandom.nextInt(lowerCaseLetters.length())));
        password.append(numbers.charAt(secureRandom.nextInt(numbers.length())));

        String allChars = upperCaseLetters + lowerCaseLetters + numbers;
        for (int i = 4; i < 15; i++) {
            password.append(allChars.charAt(secureRandom.nextInt(allChars.length())));
        }

        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = secureRandom.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }

        return new String(passwordArray);
    }
}
