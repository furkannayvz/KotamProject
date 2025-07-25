/*
package com.i2i.intern.kotam.aom.repository;

// Oracle veritabanı üzerindeki doğrulama kodu işlemlerini gerçekleştiren bir repository.

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Repository
public class VerificationCodeRepositoryOracle {

    private final Connection connection;

    public VerificationCodeRepositoryOracle(Connection connection) {
        this.connection = connection;
    }

    // VerificationCode tablosuna yeni kod ekler
    public void insertVerificationCode(String email, String code, String nationalId, LocalDateTime expirationTime) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call INSERT_VERIFICATION_CODE(?, ?, ?, ?)}");
        stmt.setString(1, email);
        stmt.setString(2, code);
        stmt.setString(3, nationalId);
        stmt.setTimestamp(4, Timestamp.valueOf(expirationTime));
        stmt.execute();
        stmt.close();
    }

    // Gelen bir kodu kontrol eder
    public boolean checkVerificationCode(String email, String code) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call CHECK_VERIFICATION_CODE(?, ?, ?)}");
        stmt.setString(1, email);
        stmt.setString(2, code);
        stmt.registerOutParameter(3, Types.INTEGER);

        stmt.execute();
        int result = stmt.getInt(3);
        stmt.close();

        return result > 0;
    }
}

 */
