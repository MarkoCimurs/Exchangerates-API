package com.exchangerates.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static final HikariConfig hikariConfig= new HikariConfig();
    private static final HikariDataSource dataSource;

    static {
        hikariConfig.setJdbcUrl("jdbc:mariadb://localhost:3306/exchangerates");
        hikariConfig.setUsername("user1");
        hikariConfig.setPassword("password1");
        dataSource = new HikariDataSource(hikariConfig);
    }

    private  DataSource (){}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
