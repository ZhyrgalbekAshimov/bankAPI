package com.bank.bankAPI.exception;

public class UserAlreadyHasThisNumberException extends RuntimeException{
    public UserAlreadyHasThisNumberException(String phoneNumber) {
        super("User already has this phone number: " + phoneNumber);
    }
}
