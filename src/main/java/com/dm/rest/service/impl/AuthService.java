package com.dm.rest.service.impl;

import com.dm.rest.exceptions.ApiException;
import com.dm.rest.payload.requests.AuthenticationRequest;
import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.payload.response.AuthenticationResponse;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.persistance.repository.UserRepository;
import com.dm.rest.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationResponse signin(AuthenticationRequest request) {
        User user = findByNameAndPassword(request.getEmail(), request.getPassword());
        String jwt = jwtTokenProvider.generateToken(user.getEmail(), user.getRoles());

        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse signup(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new ApiException("Username is already taken", HttpStatus.BAD_REQUEST);
        }
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roleService.getDefaultRole())
                .build();

        userRepository.save(user);
        String jwt = jwtTokenProvider.generateToken(user.getEmail(), user.getRoles());
        return new AuthenticationResponse(jwt);
    }

    private User findByNameAndPassword(String username, String password){
        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new ApiException("User with name '" + username + "' not found.", HttpStatus.UNPROCESSABLE_ENTITY ));

        if (!passwordEncoder.matches(password, user.getPassword())){
           throw new ApiException("Invalid password supplied", HttpStatus.BAD_REQUEST);
        }
        return user;
    }
}
