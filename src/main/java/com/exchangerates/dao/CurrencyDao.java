package com.exchangerates.dao;

import com.exchangerates.datasource.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyDao {

    public static long insertCurrencyOrGetId (String currencyCode) throws SQLException {
        Connection connection = DataSource.getConnection();

        try(PreparedStatement statement = connection.prepareStatement("""
            INSERT INTO currencies(code) VALUES (?) ON DUPLICATE KEY UPDATE  id=LAST_INSERT_ID(id), code = ?
        """, new String[]{"id"})){
            statement.setString(1, currencyCode);
            statement.setString(2, currencyCode);

            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();

            connection.close();
            return resultSet.getLong(1);

        }
    }
}

