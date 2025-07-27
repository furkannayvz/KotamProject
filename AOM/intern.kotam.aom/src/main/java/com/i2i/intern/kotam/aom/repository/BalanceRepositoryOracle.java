package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.model.Balance;
import oracle.jdbc.internal.OracleTypes;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
public class BalanceRepositoryOracle implements BalanceRepositoryBase {

    private final DataSource dataSource;

    public BalanceRepositoryOracle(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean insertCustomerPackage(String msisdn, Long packageId) {
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{call ASSIGN_PACKAGE_TO_CUSTOMER(?, ?)}")) {

            stmt.setString(1, msisdn);
            stmt.setLong(2, packageId);

            stmt.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Balance> getBalanceByMsisdn(String msisdn) {
        Balance balance = null;
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{ call GET_BALANCE_BY_MSISDN(?, ?) }")) {

            stmt.setString(1, msisdn);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();

            ResultSet rs = (ResultSet) stmt.getObject(2);
            if (rs.next()) {
                balance = new Balance();
                balance.setBalanceId(rs.getLong("BALANCE_ID"));
                balance.setMsisdn(rs.getString("MSISDN"));
                balance.setPackageId(rs.getLong("PACKAGE_ID"));
                balance.setRemainingMinutes(rs.getLong("BAL_LEFT_MINUTES"));
                balance.setRemainingSms(rs.getLong("BAL_LEFT_SMS"));
                balance.setRemainingData(rs.getLong("BAL_LEFT_DATA"));
                balance.setsDate(rs.getTimestamp("SDATE"));
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(balance);
    }

}


