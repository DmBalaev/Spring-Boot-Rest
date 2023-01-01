package com.dm.rest.service;

import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.payload.response.UserInfo;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface UserService {

    public User createUser(User user);

    User getUser(String username);

    List<User> getAllUsers();

    ApiResponse updateUser(String username, User updateUser, CustomUserDetails principal);

    ApiResponse deleteUser(String username, CustomUserDetails principal);

    ApiResponse giveAdmin(String username, CustomUserDetails currentUser);

    ApiResponse takeAdmin(String username, CustomUserDetails currentUser);

    ApiResponse setDefaultRole(String username);

    UserInfo getCurrentUser(CustomUserDetails currentUser);
    void getAdminRoleInit(String username);
}