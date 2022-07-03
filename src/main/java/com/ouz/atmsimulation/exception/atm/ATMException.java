package com.ouz.atmsimulation.exception.atm;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ATMException extends RuntimeException {

    protected String message;
    protected HttpStatus httpStatus;
    private String desc;


    public ATMException() {
        desc = "This is an exception that throws when Account operations goes wrong  !";
    }

    private ATMException(String dsc, HttpStatus status){
        super();
        this.message = "uncategorized.error";
        this.desc = dsc;
        this.httpStatus = status;
    }

    @ExceptionHandler(ATMException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ATMException internalATMException(){
        return new ATMException("No description",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ATMNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ATMException notFoundException(){

        return new ATMNotFoundException();
    }

    @ExceptionHandler(ATMNoMoneyException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ATMException noMoneyException(){

        return new ATMNoMoneyException();
    }

    @ExceptionHandler(ATMNotEnoughMoneyException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ATMException notEnoughMoneyException(){

        return new ATMNotEnoughMoneyException();
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }

    public String getDesc() {
        return desc;
    }
}
