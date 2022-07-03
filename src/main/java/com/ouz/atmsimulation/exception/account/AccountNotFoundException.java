package com.ouz.atmsimulation.exception.account;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class AccountNotFoundException extends AccountException implements Supplier<AccountNotFoundException> {
    private static final long serialVersionUID = 1L;

    public AccountNotFoundException() {
        super();
        this.message = "account.not.found";
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    @Override
    public AccountNotFoundException get() {
        return this;
    }
}
