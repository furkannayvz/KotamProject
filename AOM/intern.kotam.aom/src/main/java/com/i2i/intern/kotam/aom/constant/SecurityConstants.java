package com.i2i.intern.kotam.aom.constant;

public class SecurityConstants {

    // JWT Constants
    public static final String JWT_SECRET_KEY = "kotam-secret-key-for-jwt-token-generation-2024";
    public static final long JWT_EXPIRATION = 86400000; // 24 hours
    public static final long JWT_REFRESH_EXPIRATION = 604800000; // 7 days
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    // Security Constants
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MODERATOR = "MODERATOR";

    // Password Constants
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]";

    // Session Constants
    public static final int SESSION_TIMEOUT = 1800; // 30 minutes
    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final int LOCKOUT_DURATION = 900; // 15 minutes

    // CORS Constants
    public static final String[] ALLOWED_ORIGINS = {"http://localhost:3000", "http://localhost:8080"};
    public static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    public static final String[] ALLOWED_HEADERS = {"*"};

    // API Rate Limiting
    public static final int API_RATE_LIMIT = 100; // requests per minute
    public static final int API_BURST_CAPACITY = 200;
}
