package com.dm.rest.modelmappertest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.*;

public class ModelMapperUnitTest {
    ModelMapper mapper;

    @BeforeEach
    public void init() {
        mapper = new ModelMapper();
    }

    @Test
    public void convertDto() {
        UserTestEntity entity = new UserTestEntity(1L, "Jon", "email");

        UserDtoTest dto = mapper.map(entity, UserDtoTest.class);

        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getName()).isEqualTo(dto.getName());
        assertThat(entity.getEmail()).isEqualTo(dto.getEmail());
    }
}
