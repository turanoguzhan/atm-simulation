package com.ouz.atmsimulation.controller.dto;

import com.ouz.atmsimulation.enums.BanknoteType;
import com.ouz.atmsimulation.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ATMSuccessMessageDTO {

    private Long accountNumber;
    private HashMap<BanknoteType,Long> moneyValues;
    private BigDecimal currentBalance;
    private TransactionType transactionType;
    private LocalDateTime dateTime;
    private String message;

    @Override
    public String toString() {
        return "ATMSuccessMessageDTO{" +
                "Account Number=" + accountNumber +
                ", Money Dispersion=" + moneyValues +
                ", Current Balance=" + currentBalance +
                ", Transaction Type=" + transactionType +
                ", Date-Time=" + dateTime +
                ", Message='" + message + '\'' +
                '}';
    }
}
