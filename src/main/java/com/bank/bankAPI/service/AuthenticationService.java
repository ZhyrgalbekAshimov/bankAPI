package com.bank.bankAPI.service;

import com.bank.bankAPI.dto.UserDto;
import com.bank.bankAPI.dto.requests.SignInRequest;
import com.bank.bankAPI.dto.responses.JwtAuthenticationResponse;
import com.bank.bankAPI.entity.*;
import com.bank.bankAPI.repository.EmailRepository;
import com.bank.bankAPI.repository.PhoneNumberRepository;
import com.bank.bankAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final EmailRepository emailRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User createUser(UserDto userDto) {
        // Check if username, email, or phone number is already taken
        Optional<com.bank.bankAPI.entity.User> existingUser = userRepository.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        Optional<Email> existingeMail = emailRepository.findEmailByEmail(userDto.getEmail());
        if (existingeMail.isPresent()) {
            throw new IllegalArgumentException("Email already taken");
        }

        Optional<PhoneNumber> existingePhoneNumber = phoneNumberRepository.findPhoneNumberByNumber(userDto.getPhoneNumber());
        if (existingePhoneNumber.isPresent()) {
            throw new IllegalArgumentException("Phone number already taken");
        }

        com.bank.bankAPI.entity.User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setRole(Role.ROLE_USER);
        user.setIsActive(true);

        List<Email> emails = new ArrayList<>();
        emails.add(new Email(userDto.getEmail(), user));
        user.setEmails(emails);

        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(new PhoneNumber(userDto.getPhoneNumber(), user));
        user.setPhoneNumbers(phoneNumbers);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(userDto.getInitialDeposit());
        bankAccount.setInitialDeposit(userDto.getInitialDeposit());
        bankAccount.setUser(user);

        user.setBankAccount(bankAccount);


        return userRepository.save(user);
    }


    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}