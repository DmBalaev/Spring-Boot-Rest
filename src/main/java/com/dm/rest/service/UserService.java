package com.dm.rest.service;

import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.payload.requests.UpdateInfoRequest;
import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.payload.response.UserInfo;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.security.CustomUserDetails;

import java.util.List;

public interface UserService {

    public User createUser(RegistrationRequest request);

    User getUserByName(String username);

    List<User> getAllUsers();

    User updateUser(UpdateInfoRequest request, String email);

    ApiResponse deleteUser(String username);

    ApiResponse giveAdmin(String username);

    ApiResponse takeAdmin(String username);

    UserInfo getCurrentUser(CustomUserDetails currentUser);

}
