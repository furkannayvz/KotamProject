package com.i2i.kotam.nf;

import com.i2i.kotam.model.NotificationMessage;

import javax.sql.DataSource;
import java.sql.*;

public class OracleOperations {

    private static final DataSource dataSource = DataSourceConfig.getDataSource();

    public void callInsertProcedure(NotificationMessage message) throws SQLException {
        CallableStatement statement = null;

        try (Connection connection = dataSource.getConnection()) {
            String procedureCall = "{call INSERT_NOTIFICATION_LOG(?, ?, ?)}";
            statement = connection.prepareCall(procedureCall);

            // 1. Parametre: NOTIFICATION_TYPE (SMS / EMAIL)
            String notificationType = "EMAIL"; // Bu projede e-posta gönderildiği için sabit
            statement.setString(1, notificationType);

            // 2. Parametre: NOTIFICATION_TIME (şu anki zaman)
            statement.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));

            // 3. Parametre: MSISDN
            statement.setString(3, message.getMsisdn());

            // Prosedürü çağır
            statement.execute();
            System.out.println("Oracle procedure INSERT_NOTIFICATION_LOG executed successfully.");

        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
}