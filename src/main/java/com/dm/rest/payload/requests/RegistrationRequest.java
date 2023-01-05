package com.dm.rest.payload.requests;

import com.dm.rest.persistance.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
