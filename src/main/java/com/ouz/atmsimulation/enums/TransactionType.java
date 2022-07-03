package com.ouz.atmsimulation.enums;

public enum TransactionType {
    WITHDRAW("WITHDRAW"),
    DEPOSIT("DEPOSIT"),
    EXPOSE("EXPOSE");

    public final String value;

    TransactionType(String value) {
        this.value = value;
    }
}
