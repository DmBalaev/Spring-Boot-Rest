package com.dm.rest.service.impl;

import com.dm.rest.exceptions.ApiException;
import com.dm.rest.exceptions.UserNotFoundException;
import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.payload.response.UserInfo;
import com.dm.rest.persistance.entity.Role;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.persistance.repository.UserRepository;
import com.dm.rest.security.CustomUserDetails;
import com.dm.rest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByLogin(user.getLogin())){
            throw new ApiException("Username is already taken.", HttpStatus.BAD_REQUEST);
        }

        user.setRoles(roleService.getDefaultRole());
        user.setPassword(encoder.encode(user.getPassword()));

        log.info("created user with name'{}'", user.getLogin());
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userRepository.findAll();
    }

    @Override
    public ApiResponse updateUser(String username, User newUser) {
        User user = getUser(username);

        user.setFirstname(newUser.getFirstname());
        user.setLastname(newUser.getLastname());
        user.setPassword(newUser.getPassword());

        log.info("Update user: {} -> {}, {} -> {}, password(NoInfo) -> password(NoInfo)",
                user.getFirstname(), newUser.getFirstname(),
                user.getLastname(), newUser.getLastname());

        userRepository.save(user);
        return new ApiResponse("You successfully update info");
    }

    @Override
    public ApiResponse deleteUser(String username) {
        User user = getUser(username);

        userRepository.delete(user);
        log.info("delete user with name'{}'",  username);
        return new ApiResponse("You successfully deleted profile of: " + username);
    }

    @Override
    public ApiResponse giveAdmin(String username) {
        User user = getUser(username);
        Collection<Role> adminRole = roleService.getAdminRole();
        user.setRoles(adminRole);
        userRepository.save(user);

        log.info("Administrator rights are given to the user '{}'", username);

        return new ApiResponse("You gave ADMIN role to user: " + username);
    }

    @Override
    public ApiResponse takeAdmin(String username) {
        User user = getUser(username);
        Collection<Role> defaultRole = roleService.getDefaultRole();
        user.setRoles(defaultRole);

        log.info("Admin rights removed from user '{}'", username);

        userRepository.save(user);
        return new ApiResponse("You took ADMIN role from user: " + username);
    }

    @Override
    public ApiResponse setDefaultRole(String username) {
        User user = getUser(username);
        Collection<Role> userRole = roleService.getDefaultRole();
        user.setRoles(userRole);
        userRepository.save(user);

        log.info("{} -> set default role", username);

        return new ApiResponse("You gave DEFAULT role to user: " + username);
    }

    @Override
    public UserInfo getCurrentUser(CustomUserDetails currentUser){
        return new UserInfo(
                currentUser.getId(),
                currentUser.getLogin(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getEmail());
    }

    @Override
    public User getUser(String username){
        return userRepository.findByLogin(username)
                .orElseThrow(()-> new UserNotFoundException("User witn name '" + username + "' not found."));
    }

    public void getAdminRoleInit(String username){
        User user = getUser(username);
        Collection<Role> adminRole = roleService.getAdminRole();
        user.setRoles(adminRole);
        userRepository.save(user);

    }

    private boolean notAdmin(CustomUserDetails principal){
        return !principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    private boolean notCurrentUser(User user, CustomUserDetails principal){
        return !user.getId().equals(principal.getId());
    }
}
