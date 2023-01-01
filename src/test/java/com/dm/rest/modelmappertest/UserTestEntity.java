package com.dm.rest.modelmappertest;

import lombok.Data;

import javax.swing.*;
import java.util.Collection;

@Data
public class UserTestEntity {
    private Long id;
    private String name;
    private String email;
    private Collection<RoleTest> role;

    public UserTestEntity(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
