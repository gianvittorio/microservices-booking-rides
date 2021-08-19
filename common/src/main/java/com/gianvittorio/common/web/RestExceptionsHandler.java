package com.gianvittorio.common.web;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class RestExceptionsHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleAnyError(final Throwable error) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    public ResponseEntity<?> handleResponseStatusException(final ResponseStatusException error) {
        return ResponseEntity.status(error.getStatus())
                .body(error);
    }

    @ExceptionHandler({BindException.class, WebExchangeBindException.class, TypeMismatchException.class})
    public ResponseEntity<?> handleBindException(final Throwable error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
