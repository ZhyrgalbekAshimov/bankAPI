package com.bank.bankAPI.repository;


import com.bank.bankAPI.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// BankAccountRepository.java
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByUserId(Long userId);
    Optional<BankAccount> findBankAccountById(Long accountId);
}
