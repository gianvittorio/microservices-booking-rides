package com.gianvittorio.usersservice;

import com.gianvittorio.usersservice.exception.ApiError;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class UsersServiceExceptionsHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> handleAnyError(final Throwable error) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(error.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> handleBindException(final BindException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(error.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ApiError> handleWebExchangeBindException(final WebExchangeBindException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(error.getMessage()));
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatchExceptionException(final TypeMismatchException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(error.getMessage()));
    }
}
