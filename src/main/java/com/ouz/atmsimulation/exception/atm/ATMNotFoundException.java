package com.ouz.atmsimulation.exception.atm;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class ATMNotFoundException extends ATMException implements Supplier<ATMNotFoundException> {
    private static final long serialVersionUID = 1L;

    public ATMNotFoundException() {
        super();
        this.message = "atm.not.found.exception";
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    @Override
    public ATMNotFoundException get() {
        return new ATMNotFoundException();
    }
}
