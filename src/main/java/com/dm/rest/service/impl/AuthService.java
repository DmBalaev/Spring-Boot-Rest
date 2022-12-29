package com.dm.rest.service.impl;

import com.dm.rest.exceptions.ApiException;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.persistance.repository.RoleRepository;
import com.dm.rest.persistance.repository.UserRepository;
import com.dm.rest.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String signin(String username, String password) {
        User user = findByNameAndPassword(username, password);

        return jwtTokenProvider.generateToken(user.getLogin(), user.getRoles());
    }

    public User signup(User user) {
        if (userRepository.existsByLogin(user.getLogin())){
            throw new ApiException("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleService.getDefaultRole());

        return userRepository.save(user);
    }

    public User findByNameAndPassword(String username, String password){
        User user = userRepository.findByLogin(username)
                .orElseThrow(()-> new ApiException("User with name '" + username + "' not found.", HttpStatus.UNPROCESSABLE_ENTITY ));

        if (!passwordEncoder.matches(password, user.getPassword())){
           throw new ApiException("Invalid password supplied", HttpStatus.BAD_REQUEST);
        }
        return user;
    }
}
