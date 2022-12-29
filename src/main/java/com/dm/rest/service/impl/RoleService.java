package com.dm.rest.service.impl;

import com.dm.rest.exceptions.ApiException;
import com.dm.rest.persistance.entity.Role;
import com.dm.rest.persistance.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Collection<Role> getDefaultRole(){
        Role role = userRole();
        Collection<Role> roles = new ArrayList<>();
        roles.add(role);

        return roles;
    }

    public Collection<Role> getAdminRole(){
        Role admin = adminRole();
        Role user = userRole();

        return new ArrayList<>(List.of(admin, user));
    }

    private Role adminRole(){
        return roleRepository.findByRole("ROLE_ADMIN")
                .orElseThrow(()-> new ApiException("ADMIN role not found", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Role userRole(){
        return roleRepository.findByRole("ROLE_USER")
                .orElseThrow(()-> new ApiException("USER role not found", HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
