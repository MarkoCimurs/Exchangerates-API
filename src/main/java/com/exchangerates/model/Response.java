package com.exchangerates.model;

import com.exchangerates.dto.ExchangeRate;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.javalin.http.HttpStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
public class Response {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime timeStamp;
    protected HttpStatus status;
    protected int statusCode;
    protected String message;
    protected List<ExchangeRate> data;
}
