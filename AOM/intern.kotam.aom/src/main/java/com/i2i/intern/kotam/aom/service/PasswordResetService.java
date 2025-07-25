package com.i2i.intern.kotam.aom.service;

//bir kullanıcının doğrulama koduyla şifresini sıfırlamasını sağlayan mantığı içerir.
// Kod doğruysa → şifreyi değiştir

import com.i2i.intern.kotam.aom.repository.CustomerRepositoryOracle;
import org.springframework.stereotype.Service;
import java.sql.SQLException;


@Service
public class PasswordResetService {

    private final EmailService emailService;
    private final CustomerRepositoryOracle customerRepository;

    public PasswordResetService(EmailService emailService, CustomerRepositoryOracle customerRepository) {
        this.emailService = emailService;
        this.customerRepository = customerRepository;
    }

    public boolean resetPassword(String email, String nationalId, String code, String newPassword) {
        boolean isValid = emailService.verifyCode(email, code);
        if (!isValid) return false;

        try {
            return customerRepository.updatePasswordByEmail(email, nationalId, newPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
