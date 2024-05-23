package com.bank.bankAPI.controller;

import com.bank.bankAPI.dto.requests.SignInRequest;
import com.bank.bankAPI.dto.responses.JwtAuthenticationResponse;
import com.bank.bankAPI.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        System.out.println(111);
        return authenticationService.signIn(request);
    }

}