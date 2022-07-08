package com.ouz.atmsimulation.exception.account;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class AccountAmountException extends AccountException implements Supplier<AccountAmountException> {
    private static final long serialVersionUID = 1L;

    public AccountAmountException() {
        super();
        this.message = "account.amount.exception";
        this.httpStatus = HttpStatus.NO_CONTENT;
    }

    @Override
    public AccountAmountException get() {
        return this;
    }
}
