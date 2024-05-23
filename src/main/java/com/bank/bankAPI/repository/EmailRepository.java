package com.bank.bankAPI.repository;

import com.bank.bankAPI.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findEmailByEmail(String email);
}
