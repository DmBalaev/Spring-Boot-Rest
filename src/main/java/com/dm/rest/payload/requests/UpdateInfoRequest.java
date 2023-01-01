package com.dm.rest.payload.requests;

import com.dm.rest.persistance.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class UpdateInfoRequest {
    @NotBlank
    @Size(max = 30)
    private String firstName;
    @NotBlank
    @Size(max = 30)
    private String lasName;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public User getUser(){
        User  user = new User();
        user.setFirstname(firstName);
        user.setLastname(lasName);
        user.setPassword(password);

        return user;
    }
}
