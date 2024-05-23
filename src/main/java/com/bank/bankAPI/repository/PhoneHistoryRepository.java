package com.bank.bankAPI.repository;

import com.bank.bankAPI.entity.PhoneHistory;
import com.bank.bankAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneHistoryRepository extends JpaRepository<PhoneHistory, Long> {

}
