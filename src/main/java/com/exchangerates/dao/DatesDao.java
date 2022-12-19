package com.exchangerates.dao;

import com.exchangerates.datasource.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DatesDao {

    public static long insertDateOrGetId(LocalDateTime date) throws SQLException {
        Connection connection = DataSource.getConnection();

        try(PreparedStatement statement = connection.prepareStatement("""
            INSERT INTO dates(date) VALUES (?) ON DUPLICATE KEY UPDATE  id=LAST_INSERT_ID(id), date = ?
        """, new String[]{"id"})){

            ZoneOffset timeZoneOffset = OffsetDateTime.now().getOffset();
            Timestamp timestamp = new java.sql.Timestamp(date.toInstant(timeZoneOffset).toEpochMilli());

            statement.setTimestamp(1, timestamp);
            statement.setTimestamp(2, timestamp);

            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();

            connection.close();
            return resultSet.getLong(1);

        }
    }
}
