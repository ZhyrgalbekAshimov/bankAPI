package com.bank.bankAPI.service;

import com.bank.bankAPI.entity.PhoneHistory;
import com.bank.bankAPI.entity.User;
import com.bank.bankAPI.repository.PhoneHistoryRepository;
import com.bank.bankAPI.service.enums.HistoryAction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PhoneHistoryService {
    private final PhoneHistoryRepository phoneHistoryRepository;

    public PhoneHistoryService(PhoneHistoryRepository phoneHistoryRepository) {
        this.phoneHistoryRepository = phoneHistoryRepository;
    }

    void recordHistory(String phoneNumber, HistoryAction action, LocalDateTime dateTime, User user){
        PhoneHistory phoneHistory = new PhoneHistory();
        phoneHistory.setPhoneNumber(phoneNumber);
        phoneHistory.setAction(action);
        phoneHistory.setTimestamp(dateTime);
        phoneHistory.setUserId(user.getId());
        phoneHistoryRepository.save(phoneHistory);
    }
}
