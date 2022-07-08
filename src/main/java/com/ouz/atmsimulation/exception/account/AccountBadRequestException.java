package com.ouz.atmsimulation.exception.account;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class AccountBadRequestException extends AccountException implements Supplier<AccountBadRequestException> {
    private static final long serialVersionUID = 1L;

    public AccountBadRequestException() {
        super();
        this.message = "account.bad.request";
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    @Override
    public AccountBadRequestException get() {
        return this;
    }
}
