package com.i2i.intern.kotam.aom.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfig {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Gmail SMTP ayarları
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        // Gmail hesabı bilgileri
        mailSender.setUsername("verify.kotam@gmail.com"); // Buraya kendi mailini yaz
        mailSender.setPassword("mbmukwcrdbvptzrh");        // Gmail uygulama şifresi

        // Ek SMTP özellikleri
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // log için (isteğe bağlı)
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return mailSender;
    }
}
