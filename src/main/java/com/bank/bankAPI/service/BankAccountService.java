package com.bank.bankAPI.service;

import com.bank.bankAPI.entity.BankAccount;
import com.bank.bankAPI.entity.User;
import com.bank.bankAPI.exception.InsufficientBalanceException;
import com.bank.bankAPI.exception.SameAccountException;
import com.bank.bankAPI.exception.UserNotFoundException;
import com.bank.bankAPI.repository.BankAccountRepository;
import com.bank.bankAPI.repository.UserRepository;
import com.bank.bankAPI.service.threadLockService.ThreadLockService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

@Service
public class BankAccountService {

    //private static final Logger logger = LoggerFactory.getLogger(BankAccountService.class);
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final ThreadLockService threadLockService;

    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository, ThreadLockService threadLockService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.threadLockService = threadLockService;
    }

    public BigDecimal getAccountBalance(Long accountId) {
        Optional<BankAccount> accountOptional = bankAccountRepository.findById(accountId);
        return accountOptional.map(BankAccount::getBalance).orElse(BigDecimal.ZERO);
    }

    @Transactional
    public void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (fromUserId.equals(toUserId)) {
            throw new SameAccountException(fromUserId);
        }

        Lock fromUserLock = threadLockService.getLockForUser(fromUserId);
        Lock toUserLock = threadLockService.getLockForUser(toUserId);

        Lock firstLock = fromUserId < toUserId ? fromUserLock : toUserLock;
        Lock secondLock = fromUserId < toUserId ? toUserLock : fromUserLock;

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                User fromUser = userRepository.findById(fromUserId)
                        .orElseThrow(() -> new UserNotFoundException(fromUserId));
                User toUser = userRepository.findById(toUserId)
                        .orElseThrow(() -> new UserNotFoundException(toUserId));

                validateUser(fromUser);
                validateUser(toUser);

                BankAccount fromAccount = fromUser.getBankAccount();
                BankAccount toAccount = toUser.getBankAccount();

                validateBalance(fromAccount, amount);

                executeTransfer(fromAccount, toAccount, amount);

            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }

    private void validateUser(User user) {
        if (!user.getIsActive()) {
            throw new UserNotFoundException(user.getId());
        }
    }

    private void validateBalance(BankAccount fromAccount, BigDecimal amount) {
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(fromAccount.getBalance(), amount);
        }
    }

    private void executeTransfer(BankAccount fromAccount, BankAccount toAccount, BigDecimal amount) {
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
    }

}