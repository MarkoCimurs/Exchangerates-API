package com.exchangerates;

import com.exchangerates.service.Service;
import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        var app = Javalin.create()
                .get("/currencies", Service::getExchangeRates)
                .get("/currencies/{currency}", Service::getExchangeRate)
                .start(7070);
    }
}
