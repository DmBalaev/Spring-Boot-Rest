package com.dm.rest.service.impl;

import com.dm.rest.dto.UserDTO;
import com.dm.rest.exceptions.ApiException;
import com.dm.rest.exceptions.UserNotFoundException;
import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.persistance.entity.Role;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.persistance.repository.UserRepository;
import com.dm.rest.security.CustomUserDetails;
import com.dm.rest.service.UserService;
import com.dm.rest.util.UserConvector;
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
    private final UserConvector userConvector;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, UserConvector userConvector, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userConvector = userConvector;
        this.encoder = encoder;
    }

    @Override
    public User createUser(UserDTO userDTO, CustomUserDetails principal) {
        if (userRepository.existsByLogin(userDTO.getLogin())){
            throw new ApiException("Username is already taken.", HttpStatus.BAD_REQUEST);
        }
        User user = userConvector.convertEntity(userDTO);
        user.setRoles(roleService.getDefaultRole());
        user.setPassword(encoder.encode(userDTO.getPassword()));

        log.info("'{}', created user with name'{}'", principal, userDTO.getLogin());
        return userRepository.save(user);
    }

    @Override
    public UserDTO getUserByName(String username) {
        User user = getUser(username);

        return userConvector.convertToDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userConvector.convertAllEntity(users);
    }

    @Override
    public ApiResponse changePassword(String username, CustomUserDetails principal) {
        User user = getUser(username);

        if (notCurrentUser(user, principal) || notAdmin(principal)){
            ApiResponse response = new ApiResponse
                    ("You don't have permission to change password profile of: " + username);
            throw new AccessDeniedException(response.getMessage());
        }
        userRepository.save(user);
        return new ApiResponse("You successfully deleted");
    }

    @Override
    public ApiResponse deleteUser(String username, CustomUserDetails principal) {
        User user = getUser(username);

        if (notCurrentUser(user, principal) || notAdmin(principal)){
            ApiResponse response = new ApiResponse
                    ("You don't have permission to delete profile of: " + username);
            throw new AccessDeniedException(response.getMessage());
        }
        userRepository.delete(user);
        log.info("'{}', delete user with name'{}'", principal, username);
        return new ApiResponse("You successfully deleted profile of: " + username);
    }

    @Override
    public ApiResponse giveAdmin(String username) {
        User user = getUser(username);
        Collection<Role> adminRole = roleService.getAdminRole();
        user.setRoles(adminRole);
        userRepository.save(user);

        return new ApiResponse("You gave ADMIN role to user: " + username);
    }

    @Override
    public ApiResponse takeAdmin(String username) {
        User user = getUser(username);
        Collection<Role> defaultRole = roleService.getDefaultRole();
        user.setRoles(defaultRole);

        userRepository.save(user);
        return new ApiResponse("You took ADMIN role from user: " + username);
    }

    @Override
    public UserDTO getCurrentUser(CustomUserDetails currentUser){
        UserDTO dto = new UserDTO();
        dto.setId(currentUser.getId());
        dto.setLogin(currentUser.getUsername());
        return dto;
    }

    private User getUser(String username){
        return userRepository.findByLogin(username)
                .orElseThrow(()-> new UserNotFoundException("User witn name '" + username + "' not found."));
    }

    private boolean notAdmin(CustomUserDetails principal){
        return !principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    private boolean notCurrentUser(User user, CustomUserDetails principal){
        return !user.getId().equals(principal.getId());
    }
}
