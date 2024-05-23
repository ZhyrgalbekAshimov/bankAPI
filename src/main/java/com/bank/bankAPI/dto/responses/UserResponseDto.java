package com.bank.bankAPI.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class UserResponseDto {

    private Long userId;

    private String username;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private List<String> emails;

    private List<String> phoneNumbers;

    private String bankAccountId;

    private BigDecimal initialDeposit;

}
