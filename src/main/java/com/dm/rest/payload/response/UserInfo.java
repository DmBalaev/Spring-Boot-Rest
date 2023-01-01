package com.dm.rest.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String login;
    private String firstName;
    private String lastname;
    private String email;
}
