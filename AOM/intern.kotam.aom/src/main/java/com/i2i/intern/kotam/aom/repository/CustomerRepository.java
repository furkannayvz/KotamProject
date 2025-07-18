package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.exception.DataNotFoundException;
import com.i2i.intern.kotam.aom.helper.DatabaseConnectionManager;
import com.i2i.intern.kotam.aom.helper.VoltdbOperator; // VoltDBManager yerine VoltdbOperator
import com.i2i.intern.kotam.aom.helper.CustomPasswordEncoder;
import com.i2i.intern.kotam.aom.request.CreateBalanceRequest;
import com.i2i.intern.kotam.aom.request.RegisterCustomerRequest;
import com.i2i.intern.kotam.aom.response.AuthenticationResponse;

import com.i2i.intern.kotam.aom.service.JWTService;
import com.i2i.intern.kotam.aom.model.User;
import com.i2i.intern.kotam.aom.model.Token; // Token nesnesi
import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.enums.Role;
import com.i2i.intern.kotam.aom.enums.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.voltdb.client.ProcCallException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {

    private final DatabaseConnectionManager dbManager;
    private final VoltdbOperator voltdbOperator; // VoltDBManager yerine VoltdbOperator
    private final BalanceRepository balanceRepository;
    private final CustomPasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final TokenRepository tokenRepository; // TokenService yerine TokenRepository
    private final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);

    public CustomerRepository(DatabaseConnectionManager dbManager,
                              VoltdbOperator voltdbOperator,
                              BalanceRepository balanceRepository,
                              CustomPasswordEncoder passwordEncoder,
                              JWTService jwtService,
                              TokenRepository tokenRepository) {
        this.dbManager = dbManager;
        this.voltdbOperator = voltdbOperator;
        this.balanceRepository = balanceRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse createCustomerInOracle(RegisterCustomerRequest request) throws ClassNotFoundException, SQLException {
        logger.debug("Creating customer in Oracle DB with MSISDN: {}", request.msisdn());

        Connection connection = null;
        try {
            // Customer existence check
            if (checkCustomerExists(request.msisdn(), request.email(), request.tcNumber())) {
                logger.warn("Customer already exists with MSISDN: {}", request.msisdn());
                throw new DataNotFoundException("Customer already exists in Oracle DB"); // RuntimeException yerine DataNotFoundException
            }

            connection = dbManager.getOracleConnection();
            logger.debug("Connected to Oracle DB");

            // Get package ID
            int packageId = retrievePackageIdByName(connection, request.packageName());
            logger.debug("Retrieved package ID: {}", packageId);

            // Encrypt password
            String encodedPassword = passwordEncoder.encrypt(request.password());
            logger.debug("Password encrypted successfully");

            // Insert customer
            int customerId = insertCustomerInOracle(connection, request, encodedPassword);
            logger.debug("Customer inserted with ID: {}", customerId);

            // Create balance for customer
            createBalanceForCustomer(customerId, packageId);
            logger.debug("Balance created for customer: {}", customerId);

            // Create user object
            User user = buildUserFromRequest(request, encodedPassword, customerId);
            saveUserInOracle(connection, user);
            logger.debug("User saved successfully");

            // Generate JWT tokens
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            logger.debug("JWT tokens generated successfully");

            // Save user token - Token nesnesi oluşturarak
            saveUserToken(user, accessToken);
            logger.debug("User token saved successfully");

            logger.info("Customer created successfully in Oracle with MSISDN: {}", request.msisdn());

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (DataNotFoundException e) {
            logger.error("Customer already exists for MSISDN: {}", request.msisdn(), e);
            throw e; // DataNotFoundException'u fırlat
        } catch (Exception e) {
            logger.error("Failed to create customer in Oracle for MSISDN: {}", request.msisdn(), e);
            throw new RuntimeException("Customer creation failed: " + e.getMessage());
        } finally {
            dbManager.closeConnection(connection);
        }
    }

    public ResponseEntity<String> createCustomerInVoltDB(RegisterCustomerRequest request) throws IOException, ProcCallException, InterruptedException {
        logger.debug("Creating customer in VoltDB with MSISDN: {}", request.msisdn());

        try {
            // Get package ID from VoltDB
            int packageId = voltdbOperator.getPackageIdByName(request.packageName());
            logger.debug("Retrieved VoltDB package ID: {}", packageId);

            // Generate new customer ID
            int customerId = generateNewCustomerId();
            logger.debug("Generated new customer ID: {}", customerId);

            // Insert customer in VoltDB
            voltdbOperator.insertCustomer(
                    customerId,
                    request.name(),
                    request.surname(),
                    request.msisdn(),
                    request.email(),
                    passwordEncoder.encrypt(request.password()),
                    new Timestamp(System.currentTimeMillis()),
                    request.tcNumber()
            );

            // Create balance in VoltDB
            createBalanceInVoltDB(customerId, packageId);

            logger.info("Customer created successfully in VoltDB with MSISDN: {}", request.msisdn());
            return ResponseEntity.status(HttpStatus.CREATED).body("Customer created successfully in VoltDB");

        } catch (Exception e) {
            logger.error("Failed to create customer in VoltDB for MSISDN: {}", request.msisdn(), e);
            throw new RuntimeException("VoltDB customer creation failed: " + e.getMessage());
        }
    }

    public Optional<User> findByMsisdn(String msisdn) {
        logger.debug("Finding user by MSISDN: {}", msisdn);

        Connection connection = null;
        CallableStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getOracleConnection();
            stmt = connection.prepareCall("{call FIND_USER_BY_MSISDN(?, ?)}");
            stmt.setString(1, msisdn);
            stmt.registerOutParameter(2, Types.REF_CURSOR);

            stmt.execute();
            rs = (ResultSet) stmt.getObject(2);

            if (rs.next()) {
                User user = mapUserFromResultSet(rs);
                logger.debug("User found successfully for MSISDN: {}", msisdn);
                return Optional.of(user);
            } else {
                logger.debug("User not found for MSISDN: {}", msisdn);
                return Optional.empty();
            }

        } catch (Exception e) {
            logger.error("Error finding user by MSISDN: {}", msisdn, e);
            throw new DataNotFoundException("User not found with MSISDN: " + msisdn); // DataNotFoundException kullan
        } finally {
            closeResources(rs, stmt, connection);
        }
    }

    public void updatePasswordInOracle(String email, String tcNumber, String newPassword) throws SQLException, ClassNotFoundException {
        logger.debug("Updating password in Oracle DB for email: {}", email);

        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = dbManager.getOracleConnection();
            stmt = connection.prepareCall("{call UPDATE_CUSTOMER_PASSWORD(?, ?, ?)}");
            stmt.setString(1, email);
            stmt.setString(2, tcNumber);
            stmt.setString(3, passwordEncoder.encrypt(newPassword));

            stmt.execute();
            logger.debug("Password updated successfully for email: {}", email);

        } catch (Exception e) {
            logger.error("Error updating password for email: {}", email, e);
            throw new RuntimeException("Password update failed: " + e.getMessage());
        } finally {
            closeResources(stmt, connection);
        }
    }

    // EKSİK ÖZELLİK 1: VoltDB'de şifre güncelleme
    public void updatePasswordInVoltDB(String email, String tcNumber, String newPassword) throws IOException, InterruptedException, ProcCallException {
        logger.debug("Updating password in VoltDB for email: {}", email);

        try {
            String encryptedPassword = passwordEncoder.encrypt(newPassword);
            voltdbOperator.updatePassword(email, tcNumber, encryptedPassword);
            logger.debug("Password updated successfully in VoltDB for email: {}", email);

        } catch (Exception e) {
            logger.error("Error updating password in VoltDB for email: {}", email, e);
            throw new RuntimeException("VoltDB password update failed: " + e.getMessage());
        }
    }

    // EKSİK ÖZELLİK 2: Email ve TC ile customer kontrolü
    public boolean checkCustomerExists(String email, String tcNumber) throws SQLException, ClassNotFoundException {
        logger.debug("Checking if customer exists in Oracle with email: {} and TC: {}", email, tcNumber);

        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = dbManager.getOracleConnection();
            stmt = connection.prepareCall("{call CHECK_CUSTOMER_EXISTS_BY_MAIL_AND_TCNO(?, ?, ?)}");
            stmt.setString(1, email);
            stmt.setString(2, tcNumber);
            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.execute();

            int count = stmt.getInt(3);
            boolean exists = count > 0;

            logger.debug("Customer exists check result: {}", exists);
            return exists;

        } catch (Exception e) {
            logger.error("Error checking customer existence for email: {}", email, e);
            throw new RuntimeException("Customer existence check failed: " + e.getMessage());
        } finally {
            closeResources(stmt, connection);
        }
    }

    public void insertNotificationLog(String notificationType, Timestamp notificationTime, int customerId) throws SQLException, ClassNotFoundException {
        logger.debug("Inserting notification log for customer: {}", customerId);

        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = dbManager.getOracleConnection();
            stmt = connection.prepareCall("{call INSERT_NOTIFICATION_LOG(?, ?, ?)}");
            stmt.setString(1, notificationType);
            stmt.setTimestamp(2, notificationTime);
            stmt.setInt(3, customerId);

            stmt.execute();
            logger.debug("Notification log inserted successfully for customer: {}", customerId);

        } catch (Exception e) {
            logger.error("Error inserting notification log for customer: {}", customerId, e);
            throw new RuntimeException("Notification log insertion failed: " + e.getMessage());
        } finally {
            closeResources(stmt, connection);
        }
    }

    public List<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        logger.debug("Getting all customers from Oracle DB");

        Connection connection = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Customer> customers = new ArrayList<>();

        try {
            connection = dbManager.getOracleConnection();
            stmt = connection.prepareCall("{call GET_ALL_CUSTOMERS(?)}");
            stmt.registerOutParameter(1, Types.REF_CURSOR);

            stmt.execute();
            rs = (ResultSet) stmt.getObject(1);

            while (rs.next()) {
                Customer customer = mapCustomerFromResultSet(rs);
                customers.add(customer);
            }

            logger.debug("Retrieved {} customers from Oracle DB", customers.size());
            return customers;

        } catch (Exception e) {
            logger.error("Error getting all customers from Oracle DB", e);
            throw new RuntimeException("Failed to retrieve customers: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, connection);
        }
    }

    // Private helper methods
    private boolean checkCustomerExists(String msisdn, String email, String tcNumber) throws SQLException, ClassNotFoundException {
        logger.debug("Checking customer existence for MSISDN: {}", msisdn);

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getOracleConnection();
            stmt = connection.prepareStatement("SELECT COUNT(*) FROM CUSTOMERS WHERE MSISDN = ? OR EMAIL = ? OR TC_NUMBER = ?");
            stmt.setString(1, msisdn);
            stmt.setString(2, email);
            stmt.setString(3, tcNumber);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;

        } finally {
            closeResources(rs, stmt, connection);
        }
    }

    private int retrievePackageIdByName(Connection connection, String packageName) throws SQLException {
        logger.debug("Retrieving package ID for package: {}", packageName);

        try (CallableStatement stmt = connection.prepareCall("{call SELECT_PACKAGE_ID(?, ?)}")) {
            stmt.setString(1, packageName);
            stmt.registerOutParameter(2, Types.INTEGER);

            stmt.execute();

            int packageId = stmt.getInt(2);
            if (packageId == 0) {
                throw new DataNotFoundException("Package not found with name: " + packageName); // DataNotFoundException kullan
            }

            return packageId;
        }
    }

    private int insertCustomerInOracle(Connection connection, RegisterCustomerRequest request, String encodedPassword) throws SQLException {
        logger.debug("Inserting customer in Oracle DB");

        try (CallableStatement stmt = connection.prepareCall("{call INSERT_CUSTOMER(?, ?, ?, ?, ?, ?, ?)}")) {
            stmt.setString(1, request.name());
            stmt.setString(2, request.surname());
            stmt.setString(3, request.msisdn());
            stmt.setString(4, request.email());
            stmt.setString(5, encodedPassword);
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.setString(7, request.tcNumber());

            stmt.execute();
        }

        // Get generated customer ID
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT CUSTOMER_SEQ.CURRVAL FROM DUAL")) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new RuntimeException("Failed to retrieve customer ID");
        }
    }

    private void createBalanceForCustomer(int customerId, int packageId) throws SQLException, ClassNotFoundException {
        logger.debug("Creating balance for customer: {}", customerId);

        CreateBalanceRequest balanceRequest = CreateBalanceRequest.builder()
                .customerId(customerId)
                .packageId(packageId)
                .build();

        balanceRepository.createOracleBalance(balanceRequest);
    }

    private User buildUserFromRequest(RegisterCustomerRequest request, String encodedPassword, int customerId) {
        return User.builder()
                .userId(customerId)
                .msisdn(request.msisdn())
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .password(encodedPassword)
                .tcNumber(request.tcNumber())
                .sDate(new Timestamp(System.currentTimeMillis()))
                .role(Role.USER)
                .build();
    }

    private void saveUserInOracle(Connection connection, User user) throws SQLException {
        logger.debug("Saving user in Oracle DB: {}", user.getUserId());

        try (CallableStatement stmt = connection.prepareCall("{call INSERT_USER(?, ?, ?, ?, ?, ?, ?, ?)}")) {
            stmt.setString(1, user.getMsisdn());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getSurname());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());
            stmt.setTimestamp(6, (Timestamp) user.getSDate());
            stmt.setString(7, user.getTcNumber());
            stmt.setString(8, user.getRole().name());

            stmt.execute();
        }
    }

    // EKSİK ÖZELLİK 3: Token nesnesi oluşturarak saklama
    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .userId(user.getUserId())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.saveToken(token); // TokenRepository kullan
    }

    private int generateNewCustomerId() {
        int maxCustomerId = voltdbOperator.getMaxCustomerId();
        return maxCustomerId + 1;
    }

    private ResponseEntity<String> createBalanceInVoltDB(int customerId, int packageId) throws IOException, ProcCallException, InterruptedException {
        CreateBalanceRequest balanceRequest = CreateBalanceRequest.builder()
                .customerId(customerId)
                .packageId(packageId)
                .build();

        return balanceRepository.createVoltBalance(balanceRequest);
    }

    private User mapUserFromResultSet(ResultSet rs) throws SQLException {
        return User.builder()
                .userId(rs.getInt("ID"))
                .msisdn(rs.getString("MSISDN"))
                .name(rs.getString("FIRST_NAME"))
                .surname(rs.getString("LAST_NAME"))
                .email(rs.getString("EMAIL"))
                .password(rs.getString("PASSWORD"))
                .tcNumber(rs.getString("TC_NO"))
                .sDate(rs.getTimestamp("SDATE"))
                .role(Role.valueOf(rs.getString("ROLE")))
                .build();
    }

    private Customer mapCustomerFromResultSet(ResultSet rs) throws SQLException {
        return Customer.builder()
                .customerId(rs.getInt("CUST_ID"))
                .msisdn(rs.getString("MSISDN"))
                .name(rs.getString("NAME"))
                .surname(rs.getString("SURNAME"))
                .email(rs.getString("EMAIL"))
                .TCNumber(rs.getString("TC_NO"))
                .sDate(rs.getTimestamp("SDATE"))
                .build();
    }

    private void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    logger.error("Error closing resource", e);
                }
            }
        }
    }
}