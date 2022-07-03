package com.ouz.atmsimulation.exception.atm;

import org.springframework.http.HttpStatus;


public class ATMNoMoneyException extends ATMException {
    private static final long serialVersionUID = 1L;


    public ATMNoMoneyException() {
        super();
        this.message = "atm.has.no.money.error";
        this.httpStatus = HttpStatus.NO_CONTENT;
    }
}
