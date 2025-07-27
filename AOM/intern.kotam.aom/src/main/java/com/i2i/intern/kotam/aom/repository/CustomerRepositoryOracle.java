package com.i2i.intern.kotam.aom.repository;

import java.sql.SQLException;
import com.i2i.intern.kotam.aom.configuration.DataSourceConfig;
import com.i2i.intern.kotam.aom.dto.response.LoginResponseDTO;
import com.i2i.intern.kotam.aom.model.Customer;
import oracle.jdbc.internal.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepositoryOracle implements CustomerRepositoryBase {

    @Autowired
    private DataSource dataSource;


    /*
    public CustomerRepositoryOracle() {
        this.dataSource = DataSourceConfig.customDataSource();
    }*/

    public CustomerRepositoryOracle(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public boolean updatePasswordByEmail(String email, String nationalId, String newPassword) {
        System.out.println("Şifre update metodu çalıştı");

        String sql = "UPDATE CUSTOMER SET PASSWORD = ? WHERE EMAIL = ? AND NATIONAL_ID = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);  // PASSWORD
            stmt.setString(2, email);        // EMAIL
            stmt.setString(3, nationalId);   // NATIONAL_ID

            int updated = stmt.executeUpdate();
            System.out.println("Güncellenen satır sayısı: " + updated);

            return updated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertCustomer(String msisdn, String name, String surname, String email, String password, String nationalid) {
        String sql = "{call INSERT_CUSTOMER(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, msisdn);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, nationalid);
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Customer> loginWithMsisdnAndPassword(String msisdn, String password) {
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{call AUTHENTICATE_CUSTOMER_BY_MSISDN(?, ?, ?, ?, ?, ?, ?, ?)}")) {

            System.out.println("basladim");

            stmt.setString(1, msisdn);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, Types.VARCHAR);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.registerOutParameter(5, Types.VARCHAR);
            stmt.registerOutParameter(6, Types.VARCHAR);
            stmt.registerOutParameter(7, Types.TIMESTAMP);
            stmt.registerOutParameter(8, Types.INTEGER);

            stmt.execute();

            System.out.println("bitirdim");

            int successFlag = stmt.getInt(8);
            if (successFlag == 1) {
                Customer customer = Customer.builder()
                        .msisdn(msisdn)
                        .name(stmt.getString(3))
                        .surname(stmt.getString(4))
                        .email(stmt.getString(5))
                        .nationalId(stmt.getString(6))
                        .sDate(stmt.getTimestamp(7))
                        .build();

                return Optional.of(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Customer> getCustomerByMsisdn(String msisdn) {
        String sql = "{call GET_CUSTOMER_BY_MSISDN(?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, msisdn);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            ResultSet rs = (ResultSet) stmt.getObject(2);
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setMsisdn(rs.getString("MSISDN"));
                customer.setName(rs.getString("NAME"));
                customer.setSurname(rs.getString("SURNAME"));
                customer.setEmail(rs.getString("EMAIL"));
                customer.setsDate(rs.getTimestamp("SDATE"));
                customer.setNationalId(rs.getString("NATIONAL_ID"));
                return Optional.of(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean checkCustomerExistsByMailAndNationalId(String email, String nationalId) {
        String sql = "{call CHECK_CUSTOMER_EXISTS_BY_MAIL_AND_NATIONALID(?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, nationalId);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();
            return stmt.getInt(3) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean resetPassword(String email, String nationalId, String newPassword) {
        String sql = "{call UPDATE_CUSTOMER_PASSWORD(?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, nationalId);
            stmt.setString(3, newPassword);
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean changePassword(String email, String nationalId, String newPassword) {
        return resetPassword(email, nationalId, newPassword);
    }

    @Override
    public boolean forgotPassword(String email, String nationalId) {
        String sql = "{call SELECT_MSISDN_BY_EMAIL_AND_NATIONALID(?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, nationalId);
            stmt.registerOutParameter(3, Types.VARCHAR);
            stmt.execute();
            return stmt.getString(3) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


