/*
package com.i2i.intern.kotam.aom.service;

//bir kullanıcının doğrulama koduyla şifresini sıfırlamasını sağlayan mantığı içerir.
// Kod doğruysa → şifreyi değiştir
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

    private final EmailService emailService;

    public PasswordResetService(EmailService emailService) {
        this.emailService = emailService;
    }

    public boolean resetPassword(String email, String code, String newPassword) {
        boolean isValid = emailService.verifyCode(email, code);
        if (!isValid) return false;

        // Kullanıcıyı bulup şifreyi update etmelisin
        // örnek:
        // Optional<User> userOpt = userRepository.findByEmail(email);
        // if (userOpt.isPresent()) {
        //     user.setPassword(passwordEncoder.encode(newPassword));
        //     userRepository.save(user);
        //     return true;
        // }

        return true; // Simülasyon
    }
}

 */