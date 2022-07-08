package com.ouz.atmsimulation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalRequestHandlerException {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionMessage internalExceptionHandler(Exception ex, WebRequest request) {
        return new ExceptionMessage(
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
