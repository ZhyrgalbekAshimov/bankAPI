package com.bank.bankAPI.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class BankAccountDto {
    private BigDecimal balance;
    private BigDecimal initialDeposit;

    public BankAccountDto(BigDecimal balance, BigDecimal initialDeposit) {
        this.balance = balance;
        this.initialDeposit = initialDeposit;
    }

}
