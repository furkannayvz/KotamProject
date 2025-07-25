package com.i2i.intern.kotam.aom.repository;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

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
}


