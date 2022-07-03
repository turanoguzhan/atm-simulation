package com.ouz.atmsimulation.enums;

public enum BanknoteType {
    FIVE_BANKNOTE(5),
    TEN_BANKNOTE(10),
    TWENTY_BANKNOTE(20),
    FIFTY_BANKNOTE(50);

    public final Integer value;

    BanknoteType(Integer value) {
        this.value = value;
    }
}
