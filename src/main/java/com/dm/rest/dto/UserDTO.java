package com.dm.rest.dto;

import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.persistance.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Collection;

@Data
public class UserDTO {
    private Long id;
    private String login;

    @JsonIgnore
    private String password;
    private Collection<Role> roles;

    public UserDTO() {
    }

    public UserDTO(RegistrationRequest request) {
        this.login = request.getName();
        this.password = request.getPassword();
    }

}
