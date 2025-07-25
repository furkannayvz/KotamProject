package com.i2i.intern.kotam.aom.service;

// şifre sıfırlama akışındaki doğrulama kodu (verification code) gönderimini ve kontrolünü yöneten

import com.i2i.intern.kotam.aom.model.VerificationCode;
import com.i2i.intern.kotam.aom.repository.VerificationCodeRepositoryOracle;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final VerificationCodeRepositoryOracle verificationRepo;

    public EmailService(JavaMailSender mailSender) throws SQLException {
        this.mailSender = mailSender;
        this.verificationRepo = new VerificationCodeRepositoryOracle(); // Connection içeriden alınıyor
    }

    public String sendVerificationCode(String email, String nationalId) {
        String code = generateCode();
        String subject = "Password Recovery Code";
        String message = "Your 5-digit code: " + code;

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(message);
        mailSender.send(mail);

        try {
            verificationRepo.insertVerificationCode(email, code, nationalId, LocalDateTime.now().plusMinutes(5));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return code;
    }

    public boolean verifyCode(String email, String code) {
        try {
            return verificationRepo.checkVerificationCode(email, code);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateCode() {
        return String.valueOf(10000 + new SecureRandom().nextInt(90000));
    }
}



