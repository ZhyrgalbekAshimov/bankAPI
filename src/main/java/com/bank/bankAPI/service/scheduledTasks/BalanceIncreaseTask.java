package com.bank.bankAPI.service.scheduledTasks;

import com.bank.bankAPI.entity.User;
import com.bank.bankAPI.repository.UserRepository;
import com.bank.bankAPI.service.threadLockService.ThreadLockService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;

@Component
public class BalanceIncreaseTask {

    private static final Logger logger = LoggerFactory.getLogger(BalanceIncreaseTask.class);
    private final ThreadLockService threadLockService;
    private final UserRepository userRepository;

    public BalanceIncreaseTask(ThreadLockService threadLockService, UserRepository userRepository) {
        this.threadLockService = threadLockService;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateBalances() {

        logger.info("Scheduled task started");

        // Вытягиваем юзеров пачками чтобы избежать чрезмерной назрузки
        Pageable pageable = PageRequest.of(0, 1000);
        Page<User> page;

        do {
            page = userRepository.findAllByIsActiveTrue(pageable);
            for (User user : page) {
                Lock userLock = threadLockService.getLockForUser(user.getId());
                userLock.lock();
                BigDecimal maxBalance = user.getBankAccount().getInitialDeposit().multiply(new BigDecimal("2.07"));
                maxBalance = maxBalance.setScale(2, RoundingMode.HALF_UP);

                BigDecimal newBalance = user.getBankAccount().getBalance().multiply(new BigDecimal("1.05"));
                newBalance = newBalance.setScale(2, RoundingMode.HALF_UP);

                if (newBalance.compareTo(maxBalance) > 0) {
                    newBalance = maxBalance;
                }

                user.getBankAccount().setBalance(newBalance);
                user.getBankAccount().setLastUpdated(LocalDateTime.now());
                userRepository.save(user);
                userLock.unlock();
            }
            pageable = page.nextPageable();
        } while (page.hasNext());
        logger.info("Scheduled task completed");
    }
}
