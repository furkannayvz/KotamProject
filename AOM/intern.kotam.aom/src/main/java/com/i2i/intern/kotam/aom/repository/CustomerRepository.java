package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.exception.DataNotFoundException;
import com.i2i.intern.kotam.aom.helper.DatabaseConnectionManager;
import com.i2i.intern.kotam.aom.helper.VoltdbOperator;
import com.i2i.intern.kotam.aom.model.Customer;
import com.i2i.intern.kotam.aom.request.CreateBalanceRequest;
import com.i2i.intern.kotam.aom.request.RegisterCustomerRequest;
import com.i2i.intern.kotam.aom.helper.CustomPasswordEncoder;
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
    private final VoltdbOperator voltdbOperator;
    private final BalanceRepository balanceRepository;
    private final CustomPasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);

    public CustomerRepository(DatabaseConnectionManager dbManager,
                              VoltdbOperator voltdbOperator,
                              BalanceRepository balanceRepository,
                              CustomPasswordEncoder passwordEncoder) {
        this.dbManager = dbManager;
        this.voltdbOperator = voltdbOperator;
        this.balanceRepository = balanceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createCustomerInOracle(RegisterCustomerRequest request) throws ClassNotFoundException, SQLException {
        logger.debug("Creating customer in Oracle DB with MSISDN: {}", request.msisdn());
        Connection connection = null;
        try {
            if (checkCustomerExists(request.msisdn(), request.email(), request.tcNumber())) {
                throw new DataNotFoundException("Customer already exists");
            }

            connection = dbManager.getOracleConnection();
            int packageId = retrievePackageIdByName(connection, request.packageName());
            String encodedPassword = passwordEncoder.encrypt(request.password());
            int customerId = insertCustomerInOracle(connection, request, encodedPassword);
            createBalanceForCustomer(customerId, packageId);

        } finally {
            dbManager.closeConnection(connection);
        }
    }

    public boolean createCustomerInOracleSimple(RegisterCustomerRequest request)
            throws SQLException, ClassNotFoundException {

        Connection connection = null;
        try {
            connection = dbManager.getOracleConnection();

            String sql = "INSERT INTO CUSTOMER (MSISDN, NAME, SURNAME, EMAIL, PASSWORD, CREATED_DATE, TC_NUMBER) " +
                    "VALUES (?, ?, ?, ?, ?, SYSDATE, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, request.msisdn());
            stmt.setString(2, request.name());
            stmt.setString(3, request.surname());
            stmt.setString(4, request.email());
            stmt.setString(5, passwordEncoder.encrypt(request.password()));
            stmt.setString(6, request.tcNumber());

            int result = stmt.executeUpdate();
            stmt.close();
            return result > 0;

        } finally {
            dbManager.closeConnection(connection);
        }
    }

    public ResponseEntity<String> createCustomerInVoltDB(RegisterCustomerRequest request)
            throws IOException, ProcCallException, InterruptedException {
        int packageId = voltdbOperator.getPackageIdByName(request.packageName());
        int customerId = generateNewCustomerId();

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

        createBalanceInVoltDB(customerId, packageId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer created successfully in VoltDB");
    }

    public void updatePasswordInOracle(String email, String tcNumber, String newPassword)
            throws SQLException, ClassNotFoundException {
        Connection connection = dbManager.getOracleConnection();
        try (CallableStatement stmt = connection.prepareCall("{call UPDATE_CUSTOMER_PASSWORD(?, ?, ?)}")) {
            stmt.setString(1, email);
            stmt.setString(2, tcNumber);
            stmt.setString(3, passwordEncoder.encrypt(newPassword));
            stmt.execute();
        } finally {
            dbManager.closeConnection(connection);
        }
    }

    public void updatePasswordInVoltDB(String email, String tcNumber, String newPassword)
            throws IOException, InterruptedException, ProcCallException {
        voltdbOperator.updatePassword(email, tcNumber, passwordEncoder.encrypt(newPassword));
    }

    public boolean checkCustomerExists(String msisdn, String email, String tcNumber)
            throws SQLException, ClassNotFoundException {
        Connection connection = dbManager.getOracleConnection();
        String query = "SELECT COUNT(*) FROM CUSTOMER WHERE MSISDN = ? OR EMAIL = ? OR TC_NUMBER = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, msisdn);
            stmt.setString(2, email);
            stmt.setString(3, tcNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            return false;
        } finally {
            dbManager.closeConnection(connection);
        }
    }

    public List<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        Connection connection = dbManager.getOracleConnection();
        List<Customer> customers = new ArrayList<>();
        try (CallableStatement stmt = connection.prepareCall("{call GET_ALL_CUSTOMERS(?)}")) {
            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();
            ResultSet rs = (ResultSet) stmt.getObject(1);
            while (rs.next()) {
                customers.add(mapCustomerFromResultSet(rs));
            }
            return customers;
        } finally {
            dbManager.closeConnection(connection);
        }
    }

    public Optional<Customer> findCustomerByMsisdn(String msisdn) throws SQLException, ClassNotFoundException {
        Connection connection = dbManager.getOracleConnection();
        try (CallableStatement stmt = connection.prepareCall("{call GET_CUSTOMER_BY_MSISDN(?, ?)}")) {
            stmt.setString(1, msisdn);
            stmt.registerOutParameter(2, Types.REF_CURSOR);
            stmt.execute();
            ResultSet rs = (ResultSet) stmt.getObject(2);
            if (rs.next()) {
                return Optional.of(mapCustomerFromResultSet(rs));
            } else {
                return Optional.empty();
            }
        } finally {
            dbManager.closeConnection(connection);
        }
    }

    private int insertCustomerInOracle(Connection connection, RegisterCustomerRequest request, String encodedPassword)
            throws SQLException {
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

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT CUSTOMER_SEQ.CURRVAL FROM DUAL")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new RuntimeException("Customer ID could not be retrieved");
        }
    }

    private int retrievePackageIdByName(Connection connection, String packageName) throws SQLException {
        try (CallableStatement stmt = connection.prepareCall("{call SELECT_PACKAGE_ID(?, ?)}")) {
            stmt.setString(1, packageName);
            stmt.registerOutParameter(2, Types.INTEGER);
            stmt.execute();
            return stmt.getInt(2);
        }
    }

    private void createBalanceForCustomer(int customerId, int packageId) throws SQLException, ClassNotFoundException {
        CreateBalanceRequest balanceRequest = CreateBalanceRequest.builder()
                .customerId(customerId)
                .packageId(packageId)
                .build();
        balanceRepository.createOracleBalance(balanceRequest);
    }

    private ResponseEntity<String> createBalanceInVoltDB(int customerId, int packageId)
            throws IOException, ProcCallException, InterruptedException {
        CreateBalanceRequest balanceRequest = CreateBalanceRequest.builder()
                .customerId(customerId)
                .packageId(packageId)
                .build();
        return balanceRepository.createVoltBalance(balanceRequest);
    }

    private int generateNewCustomerId() {
        return voltdbOperator.getMaxCustomerId() + 1;
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
}
