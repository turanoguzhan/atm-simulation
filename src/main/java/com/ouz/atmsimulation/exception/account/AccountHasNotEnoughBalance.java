package com.ouz.atmsimulation.exception.account;

import org.springframework.http.HttpStatus;

public class AccountHasNotEnoughBalance extends AccountException {
    private static final long serialVersionUID = 1L;

    public AccountHasNotEnoughBalance() {
        super();
        this.message = "account.has.not.enough.balance";
        this.httpStatus = HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE;
    }
}
