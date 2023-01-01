package com.dm.rest.modelmappertest;

import lombok.Data;

@Data
public class RoleTest {
    private Long id;
    private String name;

    public RoleTest(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
