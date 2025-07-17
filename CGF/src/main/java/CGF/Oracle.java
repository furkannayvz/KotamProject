package CGF;
//import kafka.Message_name

//hikari, pom.xml eklenecek
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

//message_name

import javax.sql.DataSource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
public class Oracle {
    private static final HikariDataSource dataSource;
    //xyz, classı oluşturulacak. getProperty fonksiyonu yazılacak.
    //Böylelikle oracle url,username,pw ile bağlantı sağlanacak.
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(xyz.getProperty("oracle.url"));
        config.setUsername(xyz.getProperty("oracle.username"));
        config.setPassword(xyz.getProperty("oracle.password"));
        dataSource = new HikariDataSource(config);
    }

    //veritabanindan prosedür cagrisi yapalim.

    public static void callInsertProcedure(message_name x) {
        CallableStatement statement = null;
        try (Connection connection = dataSource.getConnection()) {
            String procedureCall = "{call insert_personal_usage(?, ?, ?, ?, ?)}";
            statement = connection.prepareCall(procedureCall);
            statement.setString(1, x.getCallerMsisdn());
            statement.setString(2, x.getCalleeMsisdn());
            statement.setTimestamp(3, x.getUsageDate());
            statement.setString(4, x.getUsageType().name());
            statement.setInt(5, x.getUsageDuration());
            /*
             * Degisken adlari küçük ihtimalle değişebilir..
             */
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();  // Hata çıktısını doğrudan konsola basar
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


/*
 * Oracle'da Oluşturma, okuma, güncelleme ve silme gibi işlemlerin
 * yapılacağı sınıf.
 * arayan id, aranan id, kullanim tipi, süresi, tarihi
 */
