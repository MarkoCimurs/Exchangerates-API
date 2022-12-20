package com.exchangerates.service;

import com.exchangerates.dao.ExchangerateDao;
import com.exchangerates.model.Response;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Service {

    public static void getExchangeRates(Context context) {
        try {
            context.json(Response.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.getCode())
                    .message("Newest exchange rates from euro")
                    .data(ExchangerateDao.getNewestCurrencyRates())
                    .build()
            );
        } catch (SQLException e) {
            context.status(500);
            context.json(Response.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.getCode())
                    .message("Ops! There was an error")
                    .build()
            );
        }
    }

    public static void getExchangeRate(Context context) {
        try {
            context.json(Response.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.getCode())
                    .message("Exchange rates from euro to " + context.pathParam("currency"))
                    .data(ExchangerateDao.getSpecificCurrency(context.pathParam("currency")))
                    .build()
            );
        } catch (SQLException e) {
            context.status(500);
            context.json(Response.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.getCode())
                    .message("Ops! There was an error")
                    .build()
            );
        }
    }
}
