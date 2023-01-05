package com.dm.rest.service;

import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.payload.requests.UpdateInfoRequest;
import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.payload.response.UserInfo;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface UserService {

    public User createUser(RegistrationRequest request);

    User getUser(String username);

    List<User> getAllUsers();

    ApiResponse updateUser(String username, UpdateInfoRequest request);

    ApiResponse deleteUser(String username);

    ApiResponse giveAdmin(String username);

    ApiResponse takeAdmin(String username);

    ApiResponse setDefaultRole(String username);

    UserInfo getCurrentUser(CustomUserDetails currentUser);
    void getAdminRoleInit(String username);
}
