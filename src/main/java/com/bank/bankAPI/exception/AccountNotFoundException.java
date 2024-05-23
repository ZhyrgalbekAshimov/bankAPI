package com.bank.bankAPI.exception;


public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long accountId) {
        super("Account not found with ID: " + accountId);
    }
}
