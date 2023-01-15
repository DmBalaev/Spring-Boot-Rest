package com.dm.rest.service.impl;

import com.dm.rest.exceptions.ApiException;
import com.dm.rest.payload.requests.RegistrationRequest;
import com.dm.rest.payload.requests.UpdateInfoRequest;
import com.dm.rest.persistance.entity.Role;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.persistance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private User user;
    @Mock
    private UserRepository repository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void init(){
        user = User.builder()
                .email("test@example.com")
                .password("hashedPassword")
                .roles(List.of(new Role("ROLE_USER")))
                .build();
    }

    @Test
    public void createUser_withCorrectData(){
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail(user.getEmail());
        request.setPassword("password");
        request.setFirstname("tets");
        request.setLastname("testLast");


        when(repository.existsByEmail(request.getEmail())).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(roleService.getDefaultRole()).thenReturn(List.of(new Role("ROLE_USER")));

        User expected = userService.createUser(request);

        verify(repository).save(any(User.class));
        assertEquals(user.getEmail(), expected.getEmail());
        assertEquals("hashedPassword", expected.getPassword());
        assertEquals(1, expected.getRoles().size());
        assertEquals("ROLE_USER", expected.getRoles().iterator().next().getRole());
    }

    @Test
    public void creatingUser_ExistingUser(){
        RegistrationRequest request = new RegistrationRequest();

        request.setEmail(user.getEmail());

        when(repository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(ApiException.class, ()-> userService.createUser(request));
    }

    @Test
    public void updateUser(){
        UpdateInfoRequest request = new UpdateInfoRequest
                ("newFirstName", "newLastName", "newPassword");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        User expected = userService.updateUser(request, user.getEmail());

        assertEquals(expected.getFirstname(), user.getFirstname());
        assertEquals(expected.getLastname(), user.getLastname());
        assertEquals(expected.getPassword(), user.getPassword());
    }

    @Test
    public void deleteUser(){
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getEmail());

        verify(repository).delete(user);
    }

    @Test
    public void giveAdmin(){

       when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
       when(roleService.getAdminRole()).thenReturn(
               new ArrayList<>(List.of(new Role("ROLE_ADMIN") , new Role("ROLE_USER"))));

       userService.giveAdmin(user.getEmail());

       assertEquals("ROLE_ADMIN", user.getRoles().iterator().next().getRole());
    }

    @Test
    public void takeAdmin(){

        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(roleService.getDefaultRole()).thenReturn(
                new ArrayList<>(List.of(new Role("ROLE_USER"))));

        userService.takeAdmin(user.getEmail());

        assertEquals("ROLE_USER", user.getRoles().iterator().next().getRole());
        assertEquals(1, user.getRoles().size());
    }

    @Test
    public void getUserByName(){

        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User expected = userService.getUserByName(user.getEmail());

        assertEquals(expected, user);
    }

}