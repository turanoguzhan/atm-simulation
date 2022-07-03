package com.ouz.atmsimulation.exception.atm;

import org.springframework.http.HttpStatus;

public class ATMNotEnoughMoneyException extends ATMException {

    private static final long serialVersionUID = 1L;

    public ATMNotEnoughMoneyException() {
        super();
        this.message = "atm.not.enough.money.error";
        this.httpStatus = HttpStatus.NOT_ACCEPTABLE;
    }
}
