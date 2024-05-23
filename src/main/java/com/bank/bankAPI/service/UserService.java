package com.bank.bankAPI.service;

import com.bank.bankAPI.entity.Email;
import com.bank.bankAPI.entity.PhoneNumber;
import com.bank.bankAPI.entity.User;
import com.bank.bankAPI.repository.EmailRepository;
import com.bank.bankAPI.repository.PhoneNumberRepository;
import com.bank.bankAPI.repository.UserRepository;
import com.bank.bankAPI.repository.specifications.UserSpecifications;
import com.bank.bankAPI.service.enums.HistoryAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;


@Service
public class UserService {

    //private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final PhoneHistoryService phoneHistoryService;
    private final EmailHistoryService emailHistoryService;
    private final EmailRepository emailRepository;



    public UserService(UserRepository userRepository, PhoneNumberRepository phoneNumberRepository, PhoneHistoryService phoneHistoryService, EmailHistoryService emailHistoryService, EmailRepository emailRepository) {
        this.userRepository = userRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.phoneHistoryService = phoneHistoryService;
        this.emailHistoryService = emailHistoryService;
        this.emailRepository = emailRepository;

    }


    @Transactional
    public User addPhoneNumber(Long userId, String number) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Проверка на существование номера у любого активного пользователя
        Optional<PhoneNumber> existingPhoneNumber = phoneNumberRepository.findPhoneNumberByNumber(number);
        if (existingPhoneNumber.isPresent()) {
            if (existingPhoneNumber.get().getUser().getUsername().equals(user.getUsername())) {
                throw new IllegalArgumentException("Number already in use by another user");
            }
            // Проверка на существование номера у данного пользователя
            throw new IllegalArgumentException("User already has this phone number");

        }

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setNumber(number);
        phoneNumber.setUser(user);

        user.getPhoneNumbers().add(phoneNumber);

        phoneHistoryService.recordHistory(number, HistoryAction.ADDED, phoneNumber.getCreatedDate(), user);

        return userRepository.save(user);


    }

    @Transactional
    public User addEmail(Long userId, String email) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Проверка на существование номера у любого активного пользователя
        Optional<Email> existingEmail = emailRepository.findEmailByEmail(email);
        if (existingEmail.isPresent()) {
            if (existingEmail.get().getUser().getUsername().equals(user.getUsername())) {
                throw new IllegalArgumentException("User already has this email");

            }
            throw new IllegalArgumentException("Email already in use by another user");

        }

        Email email_ = new Email();
        email_.setEmail(email);
        email_.setUser(user);

        user.getEmails().add(email_);

        return userRepository.save(user);

    }
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }
    @Transactional
    public boolean deletePhoneNumber(Long userId, String number) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Найти последний добавленный активный номер телефона
        PhoneNumber lastAddedNumber = user.getPhoneNumbers().stream()
                .max(Comparator.comparing(PhoneNumber::getCreatedDate))
                .orElseThrow(() -> new IllegalArgumentException("No phone numbers found for user"));
        if (number.equals(lastAddedNumber.getNumber())) {

            throw new IllegalArgumentException("Last added number cant be deleted");
        }
        for (PhoneNumber userNumber : user.getPhoneNumbers()) {
            if (userNumber.getNumber().equals(number)) {

                phoneHistoryService.recordHistory(number, HistoryAction.CHANGED, LocalDateTime.now(), user);
                phoneNumberRepository.delete(userNumber);
                return true;
            }

            return false;
        }

        return false;
    }

    @Transactional
    public boolean deleteEmail(Long userId, String email) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Найти последний добавленный email
        Email lastAddedEmail = user.getEmails().stream()
                .max(Comparator.comparing(Email::getCreatedDate))
                .orElseThrow(() -> new IllegalArgumentException("No email found for user"));
        if (email.equals(lastAddedEmail.getEmail())) {

            throw new IllegalArgumentException("Last added email cant be deleted");
        }
        for (Email userEmail : user.getEmails()) {
            if (userEmail.getEmail().equals(email)) {

                emailHistoryService.recordHistory(email, HistoryAction.CHANGED, LocalDateTime.now(), user);
                emailRepository.delete(userEmail);
                return true;
            }
            return false;
        }

        return false;
    }

    @Transactional
    public User updateUserPhoneNumber(Long userId, String oldNumber, String newNumber) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Проверка на существование номера у любого активного пользователя
        Optional<PhoneNumber> existingPhoneNumber = phoneNumberRepository.findPhoneNumberByNumber(newNumber);
        if (existingPhoneNumber.isPresent()) {
            if (existingPhoneNumber.get().getUser().getUsername().equals(user.getUsername())) {
                throw new IllegalArgumentException("User already has this phone number");

            }
            throw new IllegalArgumentException("Number already in use by another user");

        }

        for (PhoneNumber userNumber : user.getPhoneNumbers()) {
            if (userNumber.getNumber().equals(oldNumber)) {
                LocalDateTime updatedDateTime = LocalDateTime.now();
                phoneHistoryService.recordHistory(oldNumber, HistoryAction.CHANGED, updatedDateTime, user);
                phoneHistoryService.recordHistory(newNumber, HistoryAction.CHANGED, updatedDateTime, user);
                userNumber.setNumber(newNumber);
                return userRepository.save(user);
            }
        }

        throw new IllegalArgumentException("Couldn't find number to be changed");

    }

    @Transactional
    public User updateUserEmail(Long userId, String oldEmail, String newEmail) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Проверка на существование email у любого активного пользователя
        Optional<Email> existingEmail = emailRepository.findEmailByEmail(newEmail);
        if (existingEmail.isPresent()) {
            if (existingEmail.get().getUser().getUsername().equals(user.getUsername())) {
                throw new IllegalArgumentException("Email already has this phone number");

            }
            throw new IllegalArgumentException("Email already in use by another user");

        }

        for (Email email : user.getEmails()) {
            if (email.getEmail().equals(oldEmail)) {
                LocalDateTime updatedDateTime = LocalDateTime.now();
                emailHistoryService.recordHistory(oldEmail, HistoryAction.CHANGED, updatedDateTime, user);
                emailHistoryService.recordHistory(newEmail, HistoryAction.CHANGED, updatedDateTime, user);
                email.setEmail(newEmail);
                return userRepository.save(user);
            }
        }

        throw new IllegalArgumentException("Couldn't find email to be changed");

    }

    @Transactional(readOnly = true)
    public Page<User> searchUsers(LocalDate dateOfBirth, String phoneNumber, String firstName, String lastName, String email, int page, int size, String sortField, String sortDirection) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortField);
        return userRepository.findAll(UserSpecifications.filterBy(dateOfBirth, phoneNumber, firstName, lastName, email), pageable);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

}