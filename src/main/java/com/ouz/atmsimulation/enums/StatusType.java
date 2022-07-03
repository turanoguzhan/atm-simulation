package com.ouz.atmsimulation.enums;

public enum StatusType {
    ACTIVE("ACTIVE"),
    PASSIVE("PASSIVE"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED");

    public final String value;

    StatusType(String value) {
        this.value = value;
    }
}
