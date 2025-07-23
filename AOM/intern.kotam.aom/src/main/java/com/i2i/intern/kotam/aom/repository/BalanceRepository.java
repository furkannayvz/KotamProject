/*

package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.repository.BalanceRepositoryBase;
import com.i2i.intern.kotam.aom.model.Balance;
import com.i2i.intern.kotam.aom.model.PackageEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;


public class BalanceRepository implements BalanceRepositoryBase {

    private final DataSource dataSource;

    public BalanceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean insertBalance(String msisdn, Long package_id , Long left_minutes , Long left_sms , Long left_data ) {
        try (Connection connection = dataSource.getConnection()) {
            CallableStatement stmt = connection.prepareCall("{call INSERT_BALANCE(?, ?, ?, ?, ?)}");

            System.out.println("Basladim");

            stmt.setString(1, msisdn); // p_msisdn
            stmt.setLong(2, package_id); // p_package_id
            stmt.setLong(3, left_minutes); // p_left_minutes
            stmt.setLong(4, left_sms); // p_left_sms
            stmt.setLong(5, left_data); // p_left_data

            stmt.execute();

            System.out.println("Bitirdim");
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }


    public Optional<Balance> getBalanceByMsisdn(String msisdn) {
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall("{call GET_BALANCE_BY_MSISDN(?, ?)}")) {

            stmt.setString(1, msisdn);
            stmt.registerOutParameter(2, Types.REF_CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(2);
            if (rs.next()) {
                Balance balance = new Balance();
                balance.setMsisdn(rs.getString("MSISDN"));
                balance.setDataBalance(rs.getLong("BAL_LEFT_DATA"));
                balance.setSmsBalance(rs.getLong("BAL_LEFT_SMS"));
                balance.setMinutesBalance(rs.getLong("BAL_LEFT_MINUTES"));
                balance.setLastUpdate(rs.getTimestamp("SDATE"));

                PackageEntity packageEntity = new PackageEntity();
                packageEntity.setId(rs.getLong("PACKAGE_ID"));
                balance.setPackageEntity(packageEntity);

                return Optional.of(balance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean updateBalance(String msisdn, Balance updatedBalance) {
        String sql = "{call UPDATE_BALANCE_BY_MSISDN(?, ?, ?, ?)}";
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(sql)) {

            stmt.setString(1, msisdn);
            stmt.setLong(2, updatedBalance.getMinutesBalance());
            stmt.setLong(3, updatedBalance.getSmsBalance());
            stmt.setLong(4, updatedBalance.getDataBalance());

            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void updateDataBalance(String msisdn, Long data) {
        callUpdate("{call UPDATE_BALANCE_BY_MSISDN_DATA(?, ?)}", msisdn, data);
    }

    @Override
    public void updateSmsBalance(String msisdn, Long sms) {
        callUpdate("{call UPDATE_BALANCE_BY_MSISDN_SMS(?, ?)}", msisdn, sms);
    }

    public void updateMinutesBalance(String msisdn, Long minutes) {
        callUpdate("{call UPDATE_BALANCE_BY_MSISDN_MINUTES(?, ?)}", msisdn, minutes);
    }

    private void callUpdate(String sql, String msisdn, Long value) {
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(sql)) {

            stmt.setString(1, msisdn);
            stmt.setLong(2, value);
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

 */
