package com.dm.rest.controllers;

import com.dm.rest.payload.requests.AuthenticationRequest;
import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.payload.response.AuthenticationResponse;
import com.dm.rest.service.impl.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/singin")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.signin(request));
    }

    @PostMapping("/singup")
    public ResponseEntity<AuthenticationResponse> registrationUser(@Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }
}
