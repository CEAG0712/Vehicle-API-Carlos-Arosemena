package com.udacity.pricing.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Car not found")

public class PriceException extends RuntimeException {

    public PriceException(String message) {
        super(message);
    }
}
