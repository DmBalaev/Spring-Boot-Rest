package com.dm.rest.service;

import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.payload.requests.UpdateInfoRequest;
import com.dm.rest.payload.response.ApplicationResponse;
import com.dm.rest.payload.response.UserInfo;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.security.CustomUserDetails;

import java.util.List;

public interface UserService {

    public User createUser(RegistrationRequest request);

    User getUserByName(String username);

    List<User> getAllUsers();

    User updateUser(UpdateInfoRequest request, String email);

    ApplicationResponse deleteUser(String username);

    ApplicationResponse giveAdmin(String username);

    ApplicationResponse takeAdmin(String username);

    UserInfo getCurrentUser(CustomUserDetails currentUser);

}
