package com.i2i.intern.kotam.aom.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionManager.class);

    @Value("${spring.datasource.url}")
    private String oracleUrl;

    @Value("${spring.datasource.username}")
    private String oracleUsername;

    @Value("${spring.datasource.password}")
    private String oraclePassword;

    public Connection getOracleConnection() throws SQLException, ClassNotFoundException {
        logger.debug("Establishing Oracle connection...");

        Class.forName("oracle.jdbc.driver.OracleDriver");

        Connection connection = DriverManager.getConnection(
                oracleUrl,
                oracleUsername,
                oraclePassword
        );

        logger.debug("Oracle connection established successfully");
        return connection;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                logger.debug("Oracle connection closed successfully");
            } catch (SQLException e) {
                logger.error("Error closing Oracle connection", e);
            }
        }
    }

    // ✅ Yeni eklenen yardımcı metot
    public void closeResources(AutoCloseable... resources) {
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

    public boolean testConnection() {
        try (Connection connection = getOracleConnection()) {
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            logger.error("Connection test failed", e);
            return false;
        }
    }
}
