package com.dm.rest.service.impl;

import com.dm.rest.persistance.entity.Role;
import com.dm.rest.persistance.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    private RoleRepository repository;
    @InjectMocks
    public RoleService roleService;

    @Test
    void getDefaultRole() {
        Role defaaultRole = mock(Role.class);
        when(repository.findByRole("ROLE_USER")).thenReturn(Optional.of(defaaultRole));

        Collection<Role> actual = roleService.getDefaultRole();

        assertThat(actual).isNotNull()
                .contains(defaaultRole);
    }

    @Test
    void getAdminRole() {
        Role userRole = mock(Role.class);
        Role adminRole = mock(Role.class);
        when(repository.findByRole("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(repository.findByRole("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));


        Collection<Role> actual = roleService.getAdminRole();

        assertThat(actual).isNotNull()
                .contains(adminRole);
    }
}