package com.i2i.intern.kotam.aom.constant;



public class ErrorMessages {

    // General error messages
    public static final String INTERNAL_SERVER_ERROR = "Internal server error occurred";
    public static final String BAD_REQUEST = "Bad request";
    public static final String UNAUTHORIZED = "Unauthorized access";
    public static final String FORBIDDEN = "Access forbidden";
    public static final String NOT_FOUND = "Resource not found";

    // Customer error messages
    public static final String CUSTOMER_NOT_FOUND = "Customer not found";
    public static final String CUSTOMER_ALREADY_EXISTS = "Customer already exists";
    public static final String INVALID_MSISDN = "Invalid MSISDN format";
    public static final String INVALID_EMAIL = "Invalid email format";
    public static final String INVALID_TC_NUMBER = "Invalid TC number format";

    // Authentication error messages
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String TOKEN_INVALID = "Invalid token";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String PASSWORD_RESET_FAILED = "Password reset failed";

    // Balance error messages
    public static final String BALANCE_NOT_FOUND = "Balance not found";
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance";
    public static final String BALANCE_UPDATE_FAILED = "Balance update failed";
    public static final String BALANCE_CREATION_FAILED = "Balance creation failed";

    // Package error messages
    public static final String PACKAGE_NOT_FOUND = "Package not found";
    public static final String PACKAGE_INACTIVE = "Package is inactive";
    public static final String PACKAGE_EXPIRED = "Package has expired";

    // Database error messages
    public static final String DATABASE_CONNECTION_ERROR = "Database connection error";
    public static final String DATABASE_OPERATION_ERROR = "Database operation failed";
    public static final String ORACLE_CONNECTION_ERROR = "Oracle database connection error";
    public static final String VOLTDB_CONNECTION_ERROR = "VoltDB connection error";

    // Email error messages
    public static final String EMAIL_SEND_FAILED = "Failed to send email";
    public static final String EMAIL_INVALID_FORMAT = "Invalid email format";

    // Validation error messages
    public static final String VALIDATION_FAILED = "Validation failed";
    public static final String REQUIRED_FIELD_MISSING = "Required field is missing";
    public static final String FIELD_TOO_LONG = "Field value is too long";
    public static final String FIELD_TOO_SHORT = "Field value is too short";
}