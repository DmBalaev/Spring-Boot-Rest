package com.dm.rest.payload.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationRequest {
    @Email
    private String email;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
