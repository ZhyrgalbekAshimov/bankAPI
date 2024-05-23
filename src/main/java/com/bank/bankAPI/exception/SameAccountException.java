package com.bank.bankAPI.exception;

public class SameAccountException extends RuntimeException {
    public SameAccountException(Long userId) {
        super("Receiver and sender are same: " + userId);
    }
}
