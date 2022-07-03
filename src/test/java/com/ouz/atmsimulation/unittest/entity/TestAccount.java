package com.ouz.atmsimulation.unittest.entity;


import com.ouz.atmsimulation.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAccount {

    private Account account;

    @BeforeEach
    void beforeEach(){
        account = new Account();
        account.setID(1L);
        account.setAccountNumber(123456789L);
        account.setPin(1234L);
        account.setBalance(new BigDecimal("800"));
        account.setOverdraft(new BigDecimal("200"));
    }

    @Test
    void testCheckTotalAmountIsEnough(){
        assertTrue(account.checkTotalAmountIsEnough(new BigDecimal("1000")));
    }

    @Test
    void testCheckTotalAmountIsNotEnough(){
        assertFalse(account.checkTotalAmountIsEnough(new BigDecimal("1200")));
    }

    @Test
    void testValidatePinValid(){
        assertTrue(account.validatePin(1234L));
    }

    @Test
    void testValidatePinNotValid(){
        assertFalse(account.validatePin(4312L));
    }
}
