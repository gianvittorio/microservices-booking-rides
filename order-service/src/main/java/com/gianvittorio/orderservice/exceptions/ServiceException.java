package com.gianvittorio.orderservice.exceptions;

import lombok.Value;

@Value
public class ServiceException extends RuntimeException {

    private final int statusCode;

    public ServiceException(final String message, final int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
