package com.exchangerates.createDB;

import com.exchangerates.dao.ExchangerateDao;
import com.exchangerates.datasource.DataSource;
import com.exchangerates.dto.ExchangeRate;
import org.flywaydb.core.Flyway;

import java.util.List;


public class CreateDB {
    public static void main(String[] args) {
        try{
            Flyway flyway = Flyway.configure().dataSource(
                    "jdbc:mariadb://localhost:3306",
                    "user1",
                    "password1"
            ).schemas("exchangeRates").load();
            flyway.migrate();

            List<ExchangeRate> rates = XMLReader.getCurrencyData();
            ExchangerateDao.insertExchangeRates(rates);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
