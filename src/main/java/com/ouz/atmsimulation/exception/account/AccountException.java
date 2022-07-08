package com.ouz.atmsimulation.exception.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class AccountException extends RuntimeException {

    protected String message;
    protected HttpStatus httpStatus;
    protected String desc;

    public AccountException() {
        desc = "This is an exception that throws when Account operations goes wrong  !";
    }

    private AccountException(String dsc,HttpStatus status){
        super();
        this.message = "uncategorized.error";
        this.desc = dsc;
        this.httpStatus = status;
    }

    @ExceptionHandler(AccountException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AccountException internalException(){
        return new AccountException("No description",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccountHasNotEnoughBalance.class)
    @ResponseStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
    public AccountException hasNotEnoughtBalance(){
        return new AccountHasNotEnoughBalance();
    }

    @ExceptionHandler(AccountPinIncorrectException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public AccountException pinIncorrectException(){
        return new AccountPinIncorrectException();
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AccountException notFoundException(){
        return new AccountNotFoundException();
    }

    public HttpStatus getStatus() {
        return this.httpStatus;
    }

    public String getDesc(){
        return this.desc;
    }
}
