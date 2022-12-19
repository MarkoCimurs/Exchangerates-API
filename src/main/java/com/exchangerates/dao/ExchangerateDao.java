package com.exchangerates.dao;

import com.exchangerates.datasource.DataSource;
import com.exchangerates.dto.ExchangeRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExchangerateDao {

    public static List<ExchangeRate> getNewestCurrencyRates() throws SQLException {
        Connection connection = DataSource.getConnection();

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement("""
            SELECT code, MAX(date) as date, valueRate FROM currencies 
            JOIN exchangeratefromeuro e on currencies.id = e.currency_id
            JOIN dates ON e.date_id = dates.id group by code
        """)){
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                String code = resultSet.getString("code");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                float valueRate = resultSet.getFloat("valueRate");

                exchangeRates.add(new ExchangeRate(code, date, valueRate));
            }
        }
        return exchangeRates;
    }

    public static List<ExchangeRate> getSpecificCurrency(String currencyCode) throws SQLException {
        Connection connection = DataSource.getConnection();

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement("""
            SELECT code, date, valueRate FROM currencies JOIN exchangeratefromeuro e on currencies.id = e.currency_id
            JOIN dates ON e.date_id = dates.id WHERE code = ?
        """)){
            statement.setString(1, currencyCode);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                String code = resultSet.getString("code");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                float valueRate = resultSet.getFloat("valueRate");

                exchangeRates.add(new ExchangeRate(code, date, valueRate));
            }
        }
        return exchangeRates;
    }

    public static void insertExchangeRates(List<ExchangeRate> exchangeRates) throws SQLException {
        Connection connection = DataSource.getConnection();

        try(PreparedStatement statement = connection.prepareStatement("""
            INSERT INTO exchangeRateFromEuro(currency_id, date_id, valueRate) VALUES (?, ?, ?)
        """)){
            for(ExchangeRate exchangeRate: exchangeRates) {
                try {
                    long dateId = DatesDao.insertDateOrGetId(exchangeRate.getDate());
                    long currencyId = CurrencyDao.insertCurrencyOrGetId(exchangeRate.getCode());

                    statement.setLong(1, currencyId);
                    statement.setLong(2, dateId);
                    statement.setFloat(3, exchangeRate.getRate());
                    statement.execute();
                }catch (SQLException e){
                    System.out.println("Duplicate insert are ignored");
                }
            }

        }
    }
}
