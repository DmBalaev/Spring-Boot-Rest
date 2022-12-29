package com.dm.rest.service;

import com.dm.rest.dto.UserDTO;
import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.security.CustomUserDetails;

import java.util.List;

public interface UserService {

    public User createUser(UserDTO userDTO, CustomUserDetails principal);

    UserDTO getUserByName(String name);

    List<UserDTO> getAllUsers();

    ApiResponse changePassword(String name, CustomUserDetails principal);

    ApiResponse deleteUser(String name, CustomUserDetails principal);

    ApiResponse giveAdmin(String name);

    ApiResponse takeAdmin(String name);

    UserDTO getCurrentUser(CustomUserDetails currentUser);
}
