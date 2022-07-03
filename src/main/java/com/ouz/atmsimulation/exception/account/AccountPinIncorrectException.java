package com.ouz.atmsimulation.exception.account;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class AccountPinIncorrectException extends AccountException implements Supplier<AccountPinIncorrectException> {
    private static final long serialVersionUID = 1L;

    public AccountPinIncorrectException() {
        super();
        this.message = "account.invalid.pin";
        this.httpStatus = HttpStatus.FORBIDDEN;
    }

    @Override
    public AccountPinIncorrectException get() {
        return this;
    }
}