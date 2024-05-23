package com.bank.bankAPI.service;

import com.bank.bankAPI.entity.EmailHistory;
import com.bank.bankAPI.entity.User;
import com.bank.bankAPI.service.enums.HistoryAction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


// Данный сервис отвечает за историю изменений номеров телефонов у юзеров. Дополнительная опция
@Service
public class EmailHistoryService {

    void recordHistory(String email, HistoryAction action, LocalDateTime dateTime, User user) {
        EmailHistory phoneHistory = new EmailHistory();
        phoneHistory.setEmail(email);
        phoneHistory.setAction(action);
        phoneHistory.setTimestamp(dateTime);
        phoneHistory.setUserId(user.getId());
    }
}
