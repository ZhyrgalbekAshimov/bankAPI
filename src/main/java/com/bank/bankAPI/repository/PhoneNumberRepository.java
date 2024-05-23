package com.bank.bankAPI.repository;

import com.bank.bankAPI.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    Optional <PhoneNumber> findPhoneNumberByNumber(String number);
}
