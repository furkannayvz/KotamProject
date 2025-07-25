package com.i2i.kotam.nf;

import com.i2i.kotam.model.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class NotificationOperations implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(NotificationOperations.class);
    private static final Properties mailProperties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("mailConfig.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find mailConfig.properties");
            }
            mailProperties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading properties file", ex);
        }
    }

    public static String getProperty(String key) {
        String envValue = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envValue != null) {
            return envValue;
        }
        return mailProperties.getProperty(key);
    }

    public Session createMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailProperties.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", mailProperties.getProperty("mail.smtp.port"));
        props.put("mail.smtp.auth", mailProperties.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", mailProperties.getProperty("mail.smtp.starttls.enable"));

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String username = mailProperties.getProperty("mail.smtp.user");
                String password = mailProperties.getProperty("mail.smtp.password");
                return new PasswordAuthentication(username, password);
            }
        };

        return Session.getInstance(props, auth);
    }

    public void sendMail(Session session, NotificationMessage message) {
        MimeMessage msg = new MimeMessage(session);
        String fromEmail = mailProperties.getProperty("mail.smtp.user");

        try {
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.getEmail(), false));

            String subject;
            String htmlContent;

            String usageType = switch (message.getType()) {
                case VOICE -> "dakika";
                case SMS -> "sms";
                case DATA -> "internet";
            };

            if ("100".equals(message.getThreshold())) {
                subject = "Paket Kullanım Bilgilendirmesi - KOTAM";
                htmlContent = generateHtmlContentBitmis(usageType, message);
            } else {
                subject = "Kullanım Bilgilendirmesi - KOTAM";
                htmlContent = generateHtmlContentYuzde80(usageType, message);
            }

            msg.setSubject(subject, "UTF-8");
            msg.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(msg);
            logger.info("HTML email sent successfully to: " + message.getEmail());
        } catch (Exception e) {
            logger.error("Error sending HTML email", e);
        }
    }



    // 📌 Bu metod 2. adımdaki %80 kullanım bildirimi için HTML üretir
    private String generateHtmlContentYuzde80(String usageType, NotificationMessage msg) {
        return """
    <html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            body {
                margin: 0;
                padding: 0;
                background-color: #fefefe;
                font-family: 'Segoe UI', Roboto, Arial, sans-serif;
                color: #212529;
            }
            .container {
                max-width: 750px;
                margin: 30px auto;
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
            p, li {
                font-size: 16px;
                color: #212529;
                line-height: 1.5;
            }
            .alert {
                background-color: rgba(255, 243, 205, 0.8);
                padding: 15px;
                margin: 20px 0;
                font-size: 16px;
                color: #212529;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .info {
                background-color: rgba(233, 236, 239, 0.7);
                padding: 20px;
                border-radius: 8px;
                margin-top: 10px;
                color: #212529;
            }
            .info-title {
                font-weight: bold;
                margin-bottom: 10px;
                font-size: 16px;
            }
            .info ul {
                padding-left: 0;
                list-style: none;
                margin: 0;
            }
            .info li {
                color: #212529;
                font-size: 16px;
                margin-bottom: 6px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .info li strong {
            font-weight: bold;
            }
            .footer {
                margin-top: 30px;
                font-size: 12px;
                color: gray;
                text-align: center;
            }
            @media screen and (max-width: 600px) {
                .container {
                    padding: 20px;
                }
                .logo span {
                    font-size: 26px;
                    padding: 8px 20px;
                }
                p, li {
                    font-size: 15px;
                }
                .info li strong {
                    min-width: 160px;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="logo">
                <span>KOTAM</span>
            </div>
            <p><strong>Merhaba %s %s,</strong></p>
            <p><strong>%s</strong> kullanıcısı olarak, <strong>%s</strong> tarihinde yaptığınız son kullanım doğrultusunda:</p>

            <div class="alert">
                Paketinizin <strong>%s</strong> haklarını %%80 kullandınız. ⚠️
            </div>

            <div class="info">
                <div class="info-title">Kullanım bilgileriniz:</div>
                <ul>
                    <li><strong>Kalan Kullanım Hakkı:</strong> %s / %s %s</li>
                    <li><strong>Paket Başlangıç Tarihi:</strong> %s</li>
                    <li><strong>Paket Son Kullanım Tarihi:</strong> %s</li>
                </ul>
            </div>

            <div class="footer">
                Bu e-posta %s tarihinde oluşturulmuştur.
            </div>
        </div>
    </body>
    </html>
    """.formatted(
                msg.getName(),
                msg.getLastname(),
                msg.getPackageName(),
                new SimpleDateFormat("dd.MM.yyyy").format(msg.getTimestamp()),
                usageType,
                msg.getRemaining(), msg.getAmount(), usageType,
                msg.getStartDate(),
                msg.getEndDate(),
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date())
        );
    }




    // 📌 Bu metod 2. adımdaki "hak bitti" HTML mesajını üretir
    private String generateHtmlContentBitmis(String usageType, NotificationMessage msg) {
        return """
    <html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            body {
                margin: 0;
                padding: 0;
                background-color: #fefefe;
                font-family: 'Segoe UI', Roboto, Arial, sans-serif;
                color: #212529;
            }
            .container {
                max-width: 750px;
                margin: 30px auto;
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
            p, li {
                font-size: 16px;
                color: #212529;
                line-height: 1.5;
            }
            .alert {
                background-color: rgba(248, 215, 218, 0.8);
                padding: 15px;
                margin: 20px 0;
                font-size: 16px;
                color: #212529;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .info {
                background-color: rgba(233, 236, 239, 0.7);
                padding: 20px;
                border-radius: 8px;
                margin-top: 10px;
                color: #212529;
            }
            .info-title {
                font-weight: bold;
                margin-bottom: 10px;
                font-size: 16px;
            }
            .info ul {
                padding-left: 0;
                list-style: none;
                margin: 0;
            }
            .info li {
                color: #212529;
                font-size: 16px;
                margin-bottom: 6px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
            .info li strong {
            font-weight: bold;
            }
            .footer {
                margin-top: 30px;
                font-size: 12px;
                color: gray;
                text-align: center;
            }
            @media screen and (max-width: 600px) {
                .container {
                    padding: 20px;
                }
                .logo span {
                    font-size: 26px;
                    padding: 8px 20px;
                }
                p, li {
                    font-size: 15px;
                }
                .info li strong {
                    min-width: 160px;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="logo">
                <span>KOTAM</span>
            </div>
            <p><strong>Merhaba %s %s,</strong></p>
            <p><strong>%s</strong> kullanıcısı olarak, <strong>%s</strong> tarihinde yaptığınız son kullanım doğrultusunda:</p>

            <div class="alert">
                Paketinizin <strong>%s</strong> hakkı bitmiştir. ❌
            </div>

            <div class="info">
                <div class="info-title">Kullanım bilgileriniz:</div>
                <ul>
                    <li><strong>Kalan Kullanım Hakkı:</strong> 0 / %s %s</li>
                    <li><strong>Paket Başlangıç Tarihi:</strong> %s</li>
                    <li><strong>Paket Son Kullanım Tarihi:</strong> %s</li>
                </ul>
            </div>

            <div class="footer">
                Bu e-posta %s tarihinde oluşturulmuştur.
            </div>
        </div>
    </body>
    </html>
    """.formatted(
                msg.getName(),
                msg.getLastname(),
                msg.getPackageName(),
                new SimpleDateFormat("dd.MM.yyyy").format(msg.getTimestamp()),
                usageType,
                msg.getAmount(), usageType,
                msg.getStartDate(),
                msg.getEndDate(),
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date())
        );
    }


    @Override
    public void close() {}
}
