package com.dm.rest.service.impl;

import com.dm.rest.exceptions.ApiException;
import com.dm.rest.exceptions.UserNotFoundException;
import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.payload.requests.UpdateInfoRequest;
import com.dm.rest.payload.response.ApplicationResponse;
import com.dm.rest.payload.response.UserInfo;
import com.dm.rest.persistance.entity.Role;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.persistance.repository.UserRepository;
import com.dm.rest.security.CustomUserDetails;
import com.dm.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new ApiException("Username is already taken.", HttpStatus.BAD_REQUEST);
        }
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roleService.getDefaultRole())
                .build();

        log.info("created user with name'{}'", user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(UpdateInfoRequest request, String username) {
        User user = getUserByName(username);

        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLasName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        log.info("Update user with email: {}", user.getEmail());

        return userRepository.save(user);

    }

    @Override
    public ApplicationResponse deleteUser(String email) {
        User user = getUserByName(email);

        userRepository.delete(user);
        log.info("delete user with name'{}'",  email);
        return new ApplicationResponse("You successfully deleted profile of: " + email);
    }

    @Override
    public ApplicationResponse giveAdmin(String username) {
        User user = getUserByName(username);
        Collection<Role> adminRole = roleService.getAdminRole();
        user.setRoles(adminRole);
        userRepository.save(user);

        log.info("Administrator rights are given to the user '{}'", username);

        return new ApplicationResponse("You gave ADMIN role to user: " + username);
    }

    @Override
    public ApplicationResponse takeAdmin(String username) {
        User user = getUserByName(username);
        Collection<Role> defaultRole = roleService.getDefaultRole();
        user.setRoles(defaultRole);

        log.info("Admin rights removed from user '{}'", username);

        userRepository.save(user);
        return new ApplicationResponse("You took ADMIN role from user: " + username);
    }

    @Override
    public UserInfo getCurrentUser(CustomUserDetails currentUser){
        return new UserInfo(
                currentUser.getId(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getEmail());
    }

    @Override
    public User getUserByName(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User witn email '" + email + "' not found."));
    }
}
