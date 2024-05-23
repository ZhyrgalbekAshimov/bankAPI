package com.bank.bankAPI.exception;

public class PhoneNumberAlreadyTakenException extends RuntimeException {
    public PhoneNumberAlreadyTakenException(String phoneNumber) {
        super("Phone number already taken: " + phoneNumber);
    }
}
