package com.i2i.intern.kotam.aom.service;

// şifre sıfırlama akışındaki doğrulama kodu (verification code) gönderimini ve kontrolünü yöneten

import com.i2i.intern.kotam.aom.model.VerificationCode;
import java.time.format.DateTimeFormatter;
import com.i2i.intern.kotam.aom.repository.VerificationCodeRepositoryOracle;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        String subject = "Şifre Kurtarma Kodu";

        try {
            // HTML e-posta gönderimi
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(generateRecoveryHtml(code), true); // HTML içerik

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

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

    private String generateRecoveryHtml(String code) {
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        return """
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body {
                    font-family: 'Segoe UI', Roboto, Arial, sans-serif;
                    background-color: #fefefe;
                    margin: 0;
                    padding: 20px;
                }
                .container {
                    max-width: 750px;
                    margin: auto;
                    background-color: rgba(255, 255, 255, 0.95);
                    border: 2px solid #000000;
                    border-radius: 12px;
                    padding: 30px;
                    box-shadow: 0 0 20px rgba(0,0,0,0.08);
                }
                .logo {
                    text-align: center;
                    margin-bottom: 25px;
                }
                .logo span {
                    display: inline-block;
                    padding: 10px 35px;
                    font-size: 32px;
                    font-weight: bold;
                    border: 2px solid #000;
                    border-radius: 10px;
                    background-color: #ffffff;
                    color: #000000;
                }
                .message {
                    font-size: 16px;
                    margin-bottom: 20px;
                }
                .alert {
                    background-color: #fff3cd;
                    padding: 15px;
                    margin: 20px 0;
                    font-size: 18px;
                    border-left: 5px solid #ffc107;
                }
                .footer {
                    margin-top: 30px;
                    font-size: 12px;
                    color: gray;
                    text-align: center;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="logo">
                    <span>KOTAM</span>
                </div>
                <p class="message"><strong>Merhaba,</strong></p>
                <p class="message">Aşağıda şifre kurtarma işleminiz için oluşturulan doğrulama kodunu bulabilirsiniz:</p>

                <div class="alert">
                    Doğrulama Kodunuz: <strong>%s</strong>
                </div>

                <p class="message">Bu kod <strong>5 dakika</strong> boyunca geçerlidir. Güvenliğiniz için kimseyle paylaşmayınız.</p>

                <div class="footer">
                    Bu e-posta %s tarihinde oluşturulmuştur.
                </div>
            </div>
        </body>
        </html>
        """.formatted(code, formattedDate);
    }
}



