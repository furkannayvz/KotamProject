package com.i2i.intern.kotam.aom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.i2i.intern.kotam.aom.model.Token;
import com.i2i.intern.kotam.aom.model.User;
import com.i2i.intern.kotam.aom.repository.CustomerRepository;
import com.i2i.intern.kotam.aom.repository.TokenRepository;
import com.i2i.intern.kotam.aom.request.LoginCustomerRequest;
import com.i2i.intern.kotam.aom.request.RegisterCustomerRequest;
import com.i2i.intern.kotam.aom.response.AuthenticationResponse;
import com.i2i.intern.kotam.aom.response.LoginResponse;
import com.i2i.intern.kotam.aom.enums.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.voltdb.client.ProcCallException;

import java.io.IOException;
import java.sql.SQLException;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final CustomerRepository customerRepository;
    private final TokenRepository tokenRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(CustomerRepository customerRepository,
                       TokenRepository tokenRepository,
                       JWTService jwtService,
                       AuthenticationManager authenticationManager) {
        this.customerRepository = customerRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse processCustomerRegistration(RegisterCustomerRequest request)
            throws SQLException, ClassNotFoundException, IOException, ProcCallException, InterruptedException {

        logger.info("Processing customer registration for MSISDN: {}", request.msisdn());

        // Oracle'da müşteri kaydı
        AuthenticationResponse oracleResponse = customerRepository.createCustomerInOracle(request);

        // VoltDB'de müşteri kaydı
        ResponseEntity<String> voltResponse = customerRepository.createCustomerInVoltDB(request);
        if (!voltResponse.getStatusCode().is2xxSuccessful()) {
            logger.error("VoltDB registration failed for MSISDN: {}", request.msisdn());
            throw new RuntimeException("VoltDB registration failed for MSISDN: " + request.msisdn());
        }

        // Hazelcast cache'e kaydet (dependency gelince aktif edilecek)
        // HazelcastMWOperation.put(request.msisdn(), request.msisdn());

        logger.info("Customer registration completed for MSISDN: {}", request.msisdn());
        return oracleResponse;
    }

    public LoginResponse authenticateCustomer(LoginCustomerRequest request)
            throws SQLException, ClassNotFoundException {

        logger.info("Authenticating customer: {}", request.msisdn());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.msisdn(), request.password())
        );

        var user = customerRepository.findByMsisdn(request.msisdn())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var accessToken = jwtService.createAccessToken(user);
        var refreshToken = jwtService.createRefreshToken(user);

        invalidateUserTokens(user);
        storeUserToken(user, accessToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void handleTokenRefresh(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String msisdn;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        msisdn = jwtService.retrieveMsisdnFromToken(refreshToken);

        if (msisdn != null) {
            var user = customerRepository.findByMsisdn(msisdn)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (jwtService.validateTokenForUser(refreshToken, user)) {
                var newAccessToken = jwtService.createAccessToken(user);
                invalidateUserTokens(user);
                storeUserToken(user, newAccessToken);

                var authResponse = LoginResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void invalidateUserTokens(User user) throws SQLException, ClassNotFoundException {
        var validTokens = tokenRepository.retrieveValidTokensForUser(user.getUserId());
        if (validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
            tokenRepository.modifyTokenStatus(token);
        });
    }

    private void storeUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .userId(user.getUserId())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.saveToken(token);
    }
}
