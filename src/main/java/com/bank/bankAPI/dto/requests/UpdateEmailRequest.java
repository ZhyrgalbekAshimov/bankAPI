package com.bank.bankAPI.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmailRequest {

    @Email
    private String oldEmail;

    @Email
    private String newEmail;

}
