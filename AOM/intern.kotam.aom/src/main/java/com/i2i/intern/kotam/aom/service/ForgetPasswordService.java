package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.helper.CustomPasswordEncoder;
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
    private final CustomPasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final DatabaseConnectionManager dbManager;

    public ForgetPasswordService(CustomerRepository customerRepository,
                                 CustomPasswordEncoder passwordEncoder,
                                 JavaMailSender mailSender,
                                 DatabaseConnectionManager dbManager) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.dbManager = dbManager;
    }

    public ResponseEntity<String> processPasswordReset(ForgetPasswordRequest request) {
        try {
            logger.info("Processing password reset for email: {}", request.email());

            if (!validateCustomerExistence(request.email(), request.TCNumber())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer does not exist");
            }

            String newPassword = createRandomPassword();
            String encryptedPassword = passwordEncoder.encrypt(newPassword);

            try {
                dispatchPasswordEmail(request.email(), newPassword);
            } catch (SendFailedException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send email");
            } catch (MessagingException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while sending email");
            }

            try {
                executePasswordUpdate(request.email(), request.TCNumber(), encryptedPassword);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while resetting password");
            }

            int customerId = fetchCustomerIdFromDatabase(request.email(), request.TCNumber());
            try {
                recordNotificationActivity(new Timestamp(System.currentTimeMillis()), customerId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while inserting notification logs");
            }

            logger.info("Password reset completed for email: {}", request.email());
            return ResponseEntity.ok("Please check your mail address.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred while resetting password");
        }
    }

    private boolean validateCustomerExistence(String email, String tcNumber) throws Exception {
        return customerRepository.checkCustomerExists(email, tcNumber);
    }

    private void executePasswordUpdate(String email, String tcNumber, String encryptedPassword) throws Exception {
        customerRepository.updatePasswordInOracle(email, tcNumber, encryptedPassword);
        customerRepository.updatePasswordInVoltDB(email, tcNumber, encryptedPassword);
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

    private int fetchCustomerIdFromDatabase(String email, String tcNumber) throws SQLException, ClassNotFoundException {
        Connection connection = dbManager.getOracleConnection();
        CallableStatement stmt = connection.prepareCall("{call SELECT_CUSTOMER_ID_BY_EMAIL_AND_TCNUMBER(?, ?, ?)}");
        stmt.setString(1, email);
        stmt.setString(2, tcNumber);
        stmt.registerOutParameter(3, Types.INTEGER);
        stmt.execute();
        int customerId = stmt.getInt(3);
        stmt.close();
        connection.close();
        return customerId;
    }

    private void recordNotificationActivity(Timestamp notificationTime, int customerId) throws Exception {
        customerRepository.insertNotificationLog("Password Reset", notificationTime, customerId); // DÜZELTİLEN SATIR
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
