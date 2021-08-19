package com.gianvittorio.orderservice.exceptions;

public class UserNotFoundException extends ServiceException {

    public UserNotFoundException(final String message, final int statusCode) {
        super(message, statusCode);
    }
}
