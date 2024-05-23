package com.bank.bankAPI.controller;

import com.bank.bankAPI.dto.requests.TransferRequest;
import com.bank.bankAPI.service.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{fromUserId}/transfer/{toUserId}")
    public ResponseEntity<Void> transferMoney(
            @PathVariable Long fromUserId,
            @PathVariable Long toUserId,
            @RequestBody TransferRequest transferRequest) {
            bankAccountService.transferMoney(fromUserId, toUserId, transferRequest.getAmount());
            return ResponseEntity.ok().build();

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{bankAccountId}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable Long bankAccountId) {
        BigDecimal balance = bankAccountService.getAccountBalance(bankAccountId);
        return ResponseEntity.ok(balance);
    }
}
