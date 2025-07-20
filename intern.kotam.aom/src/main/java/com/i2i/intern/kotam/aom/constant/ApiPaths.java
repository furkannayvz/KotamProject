package com.i2i.intern.kotam.aom.constant;

public class ApiPaths {

    public static final String API_VERSION = "/v1/api";

    // Auth endpoints
    public static final String AUTH_BASE = API_VERSION + "/auth";
    public static final String AUTH_LOGIN = AUTH_BASE + "/login";
    public static final String AUTH_REGISTER = AUTH_BASE + "/register";
    public static final String AUTH_REFRESH_TOKEN = AUTH_BASE + "/refresh-token";
    public static final String AUTH_LOGOUT = AUTH_BASE + "/logout";

    // Customer endpoints
    public static final String CUSTOMER_BASE = API_VERSION + "/customer";
    public static final String CUSTOMER_GET_ALL = CUSTOMER_BASE + "/getAllCustomers";
    public static final String CUSTOMER_GET_BY_MSISDN = CUSTOMER_BASE + "/getCustomerByMsisdn";
    public static final String CUSTOMER_REGISTER = CUSTOMER_BASE + "/register";
    public static final String CUSTOMER_UPDATE_PASSWORD = CUSTOMER_BASE + "/password";

    // Balance endpoints
    public static final String BALANCE_BASE = API_VERSION + "/balance";
    public static final String BALANCE_REMAINING = BALANCE_BASE + "/remainingBalance";
    public static final String BALANCE_ORACLE = BALANCE_BASE + "/oracle";
    public static final String BALANCE_CREATE = BALANCE_BASE + "/create";
    public static final String BALANCE_UPDATE = BALANCE_BASE + "/update";

    // Package endpoints
    public static final String PACKAGE_BASE = API_VERSION + "/packages";
    public static final String PACKAGE_GET_ALL = PACKAGE_BASE + "/getAllPackages";
    public static final String PACKAGE_GET_BY_MSISDN = PACKAGE_BASE + "/getUserPackageByMsisdn";
    public static final String PACKAGE_GET_DETAILS = PACKAGE_BASE + "/getPackageDetails";

    // Forget Password endpoints
    public static final String FORGET_PASSWORD_BASE = API_VERSION + "/forgetPassword";
    public static final String FORGET_PASSWORD_RESET = FORGET_PASSWORD_BASE + "/reset";
}