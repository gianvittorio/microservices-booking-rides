package com.gianvittorio.common.web;

import com.gianvittorio.common.exceptions.DriverNotFoundException;
import com.gianvittorio.common.exceptions.ServiceException;
import com.gianvittorio.common.exceptions.UserNotFoundException;
import com.gianvittorio.common.web.dto.ApiError;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ControllerAdvice
@ResponseBody
@Log4j2
public class RestExceptionsHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleAnyError(final Throwable error) {
        return ApiError.builder()
                .createdAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .status(HttpStatus.BAD_REQUEST.value())
                .message(error.getMessage())
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(final UserNotFoundException error) {
        return ApiError.builder()
                .createdAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .status(HttpStatus.NOT_FOUND.value())
                .message(error.getMessage())
                .build();
    }

    @ExceptionHandler(DriverNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleDriverNotFoundException(final DriverNotFoundException error) {
        return ApiError.builder()
                .createdAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .status(HttpStatus.NOT_FOUND.value())
                .message(error.getMessage())
                .build();
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleServiceException(final ServiceException error) {
        return ApiError.builder()
                .createdAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(error.getMessage())
                .build();
    }
}
