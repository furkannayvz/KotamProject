package com.i2i.intern.kotam.aom.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


public class DataSourceConfig {

    public static DataSource customDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:oracle:thin:@//44.222.180.70:1521/XE");
        ds.setUsername("SYSTEM");
        ds.setPassword("Kotam123");
        ds.setDriverClassName("oracle.jdbc.OracleDriver");
        return ds;
    }

}
