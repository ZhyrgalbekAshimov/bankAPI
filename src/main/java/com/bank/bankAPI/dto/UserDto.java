package com.bank.bankAPI.dto;

import com.bank.bankAPI.dto.validator.AtLeastOneField;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AtLeastOneField
public class UserDto {

    @NotEmpty
    private String username;

    @NotEmpty
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    private String password;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotNull
    private LocalDate dateOfBirth;

    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;
    @Pattern(regexp = "^\\+\\d+$", message = "Invalid phone number. Number format should be +0000000...")
    private String phoneNumber;
    private BankAccountDto bankAccount;

    private BigDecimal initialDeposit;

    public UserDto(String username, String password, String fullName, LocalDate dateOfBirth, String email, String phoneNumber, BankAccountDto bankAccount) {
        this.username = username;
        this.password = password;
        this.firstName = fullName;
        this.lastName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bankAccount = bankAccount;
    }

}
