package com.bank.bankAPI.entity;

import com.bank.bankAPI.service.enums.HistoryAction;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity

@Setter
@Getter
public class PhoneHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String phoneNumber;
    private HistoryAction action;
    private LocalDateTime timestamp;
}
