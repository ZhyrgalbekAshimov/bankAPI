package com.bank.bankAPI.dto.requests;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePhoneNumberRequest {

    @Pattern(regexp = "^\\+\\d+$", message = "Invalid phone number. Number format should be +0000000...")
    private String oldNumber;

    @Pattern(regexp = "^\\+\\d+$", message = "Invalid phone number. Number format should be +0000000...")
    private String newNumber;

}
