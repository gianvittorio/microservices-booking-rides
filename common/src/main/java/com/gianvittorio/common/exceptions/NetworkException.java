package com.gianvittorio.common.exceptions;

import lombok.Value;

@Value
public class NetworkException extends RuntimeException {

    public NetworkException(final String message) {
        super(message);
    }

    public NetworkException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NetworkException(final Throwable cause) {
        super(cause);
    }
}
