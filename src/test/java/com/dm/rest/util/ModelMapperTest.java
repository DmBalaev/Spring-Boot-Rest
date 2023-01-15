package com.dm.rest.util;

import com.dm.rest.payload.response.UserInfo;
import com.dm.rest.persistance.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelMapperTest {
    private ModelMapper mapper;

    @BeforeEach
    public void init(){
        mapper = new ModelMapper();
    }

    @Test
    void convertToDto() {
        User user = User.builder()
                .id(1L)
                .firstname("test")
                .lastname("test2")
                .email("test@example.com")
                .build();

        UserInfo info = mapper.map(user, UserInfo.class);

        assertEquals(info.getId(), user.getId());
        assertEquals(user.getEmail(), info.getEmail());
        assertEquals(user.getFirstname(), info.getFirstName());
        assertEquals(user.getLastname(), info.getLastname());
    }
}