package com.ouz.atmsimulation.enums;

public enum SourceType {
    ATM("ATM"),
    ACCOUNT("ACCOUNT");


    public final String value;

    SourceType(String value) {
        this.value = value;
    }
}
