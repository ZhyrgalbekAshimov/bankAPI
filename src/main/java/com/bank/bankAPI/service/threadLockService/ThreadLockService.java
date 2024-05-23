package com.bank.bankAPI.service.threadLockService;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ThreadLockService {

    // Данный мап хранит юзеров.
    // Другие сервисы обращаются к нему чтобы посмотреть не заблокирован ли пользователь в данный момент другим потоком
    public final ConcurrentHashMap<Long, Lock> userLocks = new ConcurrentHashMap<>();
    public Lock getLockForUser(Long userId) {
        return userLocks.computeIfAbsent(userId, k -> new ReentrantLock());
    }
}
