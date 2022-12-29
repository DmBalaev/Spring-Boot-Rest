package com.dm.rest.payload.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private String name;
    private String password;
}
