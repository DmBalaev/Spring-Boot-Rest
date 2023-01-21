package com.dm.rest.controllers;

import com.dm.rest.payload.requests.AuthenticationRequest;
import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.payload.response.AuthenticationResponse;
import com.dm.rest.service.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Operation(
            tags = {"Registration and authentication"},
            summary = "Authentication user",
            description = "This is endpoint for authentication user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "This is the request body",
                    content = @Content(schema = @Schema)
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Wrong password specified", content = @Content),
                    @ApiResponse(responseCode = "422", description = "User does not exist", content = @Content)
            },
            security = {@SecurityRequirement(name = "BearerJWT")}
    )
    public ResponseEntity<AuthenticationResponse> authenticateUser(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.signin(request));
    }

    @PostMapping("/singup")
    @Operation(
            tags = {"Registration and authentication"},
            summary = "Registration user",
            description = "This is endpoint for registration user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registration successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Username is already taken", content = @Content)
            }
    )
    public ResponseEntity<AuthenticationResponse> registrationUser(@Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }
}
