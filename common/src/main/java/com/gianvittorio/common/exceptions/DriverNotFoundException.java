package com.gianvittorio.common.exceptions;

public class DriverNotFoundException extends ServiceException{

    public DriverNotFoundException(String message, int statusCode) {
        super(message, statusCode);
    }
}
