package com.dm.rest.controllers;

import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.payload.response.JwtAuthenticationResponse;
import com.dm.rest.payload.requests.LoginRequest;
import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.persistance.entity.User;
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
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/singin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.signin(loginRequest.getName(), loginRequest.getPassword());

        return new ResponseEntity<>(new JwtAuthenticationResponse(token), HttpStatus.OK);
    }

    @PostMapping("/singup")
    public ResponseEntity<ApiResponse> registrationUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        authService.signup(new User(registrationRequest.getName(), registrationRequest.getPassword()));

        return new ResponseEntity<>(new ApiResponse("User registered successfully"), HttpStatus.OK);
    }
}
