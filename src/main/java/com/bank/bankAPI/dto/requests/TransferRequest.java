package com.bank.bankAPI.dto.requests;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {
    private BigDecimal amount;
}
