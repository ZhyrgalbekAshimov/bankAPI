package com.bank.bankAPI.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(BigDecimal balance, BigDecimal amount) {
        super(String.format("Insufficient balance. Current balance: %s, Transfer amount: %s", balance, amount));
    }
}
