package com.bank.bankAPI.controller;

import com.bank.bankAPI.service.AuthenticationService;
import com.bank.bankAPI.service.JwtService;
import com.bank.bankAPI.dto.UserDto;
import com.bank.bankAPI.dto.requests.UpdateEmailRequest;
import com.bank.bankAPI.dto.requests.UpdatePhoneNumberRequest;
import com.bank.bankAPI.dto.responses.UserResponseDto;
import com.bank.bankAPI.entity.User;
import com.bank.bankAPI.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    JwtService jwtUserDetailsService;

    public UserController(UserService userService, AuthenticationService authenticationService, JwtService jwtUserDetailsService) {
        this.authenticationService = authenticationService;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserDto userDto) {
        User user = authenticationService.createUser(userDto);
        UserResponseDto userResponseDto = user.toUserResponseDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{userId}/phone")
    public ResponseEntity<UserResponseDto> addPhoneNumber(@PathVariable Long userId, @RequestBody @Pattern(regexp = "^\\+\\d+$", message = "Invalid phone number. Number format should be +0000000...") String phoneNumber) {
        User user = userService.addPhoneNumber(userId, phoneNumber);
        UserResponseDto userResponseDto = user.toUserResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{userId}/email")
    public ResponseEntity<UserResponseDto> addEmail(@PathVariable Long userId, @RequestBody @Email String email) {
        User user = userService.addEmail(userId, email);
        UserResponseDto userResponseDto = user.toUserResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{userId}/phone")
    public ResponseEntity<UserResponseDto> updateUserPhoneNumber(@PathVariable Long userId, @RequestBody UpdatePhoneNumberRequest updatePhoneNumberRequest) {
        User updatedUser = userService.updateUserPhoneNumber(userId, updatePhoneNumberRequest.getOldNumber(), updatePhoneNumberRequest.getNewNumber());
        UserResponseDto userResponseDto = updatedUser.toUserResponseDto();
        return ResponseEntity.ok(userResponseDto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{userId}/email")
    public ResponseEntity<UserResponseDto> updateUserEmail(@PathVariable Long userId, @RequestBody UpdateEmailRequest updateEmailRequest) {
        User updatedUser = userService.updateUserEmail(userId, updateEmailRequest.getOldEmail(), updateEmailRequest.getNewEmail());
        UserResponseDto userResponseDto = updatedUser.toUserResponseDto();
        return ResponseEntity.ok(userResponseDto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{userId}/phone")
    public ResponseEntity<User> deleteUserPhoneNumber(@PathVariable Long userId,
                                                      @RequestParam @Pattern(regexp = "^\\+\\d+$", message = "Invalid phone number. Number format should be +0000000...") String phoneNumber) {
        boolean isDeleted = userService.deletePhoneNumber(userId, phoneNumber);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{userId}/email")
    public ResponseEntity<User> deleteUserEmail(@PathVariable Long userId,
                                                @RequestParam @Email String email) {
        boolean isDeleted = userService.deleteEmail(userId, email);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<UserResponseDto> searchUsers(
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        LocalDate parsedDateOfBirth = dateOfBirth != null ? LocalDate.parse(dateOfBirth, DateTimeFormatter.ISO_DATE) : null;
        Page<User> users = userService.searchUsers(parsedDateOfBirth, phoneNumber, firstName, lastName, email, page, size, sortField, sortDirection);
        return users.map(User::toUserResponseDto);
    }
}