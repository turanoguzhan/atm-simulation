package com.ouz.atmsimulation.service;

import com.ouz.atmsimulation.enums.TransactionType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface ATMService {

    ResponseEntity exposeAccount(Long accountNumber, Long pin);

    ResponseEntity atmOperation(Long accountNumber, Long pin, BigDecimal amount, TransactionType type);

}
