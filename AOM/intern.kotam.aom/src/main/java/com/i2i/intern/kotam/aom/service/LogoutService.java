package com.i2i.intern.kotam.aom.service;

import com.i2i.intern.kotam.aom.model.Token;
import com.i2i.intern.kotam.aom.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogoutService.class);
    private final TokenRepository tokenRepository;

    public LogoutService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        logger.debug("Logging out user");
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Authorization header is missing or invalid");
            return;
        }

        String jwt = authHeader.substring(7);

        tokenRepository.searchTokenByValue(jwt).ifPresent(token -> {
            logger.debug("Revoking and expiring token: {}", maskToken(jwt));
            token.setRevoked(true);
            token.setExpired(true);
            tokenRepository.modifyTokenStatus(token); // ✔ doğru metot
        });

        logger.debug("Logout process completed");
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 8) return "***";
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }
}
