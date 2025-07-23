package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.configuration.DataSourceConfig;
import com.i2i.intern.kotam.aom.model.Customer;
import oracle.jdbc.internal.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
public class CustomerRepositoryOracle implements CustomerRepositoryBase {

    @Autowired
    private DataSource dataSource;

    public CustomerRepositoryOracle() {
        // Manuel olarak kendi DataSource'unu oluştur
        this.dataSource = DataSourceConfig.customDataSource();
    }

    @Override
    public boolean insertCustomer( String msisdn,String name,String surname, String email, String password,String nationalid) {
        String sql = "{call INSERT_CUSTOMER(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            System.out.println("Basladim");

            stmt.setString(1, msisdn);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, nationalid);

            stmt.execute();

            System.out.println("bitirdim");

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Customer> authenticateCustomer(String msisdn, String password) {
        String sql = "{call AUTHENTICATE_CUSTOMER(?, ?, ?)}"; // 3 parametre

        try (Connection conn = dataSource.getConnection()) {
            CallableStatement stmt = conn.prepareCall(sql);

            System.out.println("Basladim");

            stmt.setString(1, msisdn);
            stmt.setString(2, password);
            stmt.registerOutParameter(3, Types.INTEGER); // p_result OUT

            stmt.execute();

            System.out.println("Bitirdim");

            int result = stmt.getInt(3);
            if (result == 1) {
                return getCustomerByMsisdn(msisdn);
            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    @Override
    public boolean checkCustomerExistsByMailAndNationalId(String email, String nationalId) {
        String sql = "{call CHECK_CUSTOMER_EXISTS_BY_MAIL_AND_NATIONALID(?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            System.out.println("Basladim");

            stmt.setString(1, email);
            stmt.setString(2, nationalId);
            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.execute();

            System.out.println("Bitirdim");

            return stmt.getInt(3) > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Customer> getCustomerByMsisdn(String msisdn) {
        String sql = "{call GET_CUSTOMER_BY_MSISDN(?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            System.out.println("Basladim");

            stmt.setString(1, msisdn);
            stmt.registerOutParameter(2, OracleTypes.CURSOR); // OracleTypes import edilmeli

            stmt.execute();



            ResultSet rs = (ResultSet) stmt.getObject(2); // OUT parametreyi al
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setMsisdn(rs.getString("MSISDN"));
                customer.setName(rs.getString("NAME"));
                customer.setSurname(rs.getString("SURNAME"));
                customer.setEmail(rs.getString("EMAIL"));
                //customer.setPassword(rs.getString("PASSWORD"));
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
    public boolean resetPassword(String email, String nationalId, String newPassword) {
        String sql = "{call UPDATE_CUSTOMER_PASSWORD(?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            System.out.println("Basladim");

            stmt.setString(1, email);
            stmt.setString(2, nationalId);
            stmt.setString(3, newPassword);

            stmt.execute();

            System.out.println("bitirdim");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean changePassword(String email, String nationalId, String newPassword) {
        String sql = "{call UPDATE_CUSTOMER_PASSWORD(?, ?, ?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            System.out.println("Basladim");

            stmt.setString(1, email);
            stmt.setString(2, nationalId);
            stmt.setString(3, newPassword);

            stmt.execute();

            System.out.println("bitirdim");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean forgotPassword(String email, String nationalId) {
        String procedure = "{ call SELECT_MSISDN_BY_EMAIL_AND_NATIONALID(?, ?, ?) }";
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall(procedure)) {

            System.out.println("basladim");

            stmt.setString(1, email);
            stmt.setString(2, nationalId);
            stmt.registerOutParameter(3, Types.VARCHAR); // OUT parametre MSISDN

            stmt.execute();

            System.out.println("bitirdim");
            String msisdn = stmt.getString(3); // OUT değeri al

            return msisdn != null; // Eğer varsa true

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
