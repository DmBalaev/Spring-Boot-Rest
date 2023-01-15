package com.dm.rest.util;

import com.dm.rest.payload.response.UserInfo;
import com.dm.rest.persistance.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserConvectorTest {
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private UserConvector convector;

    @Test
    void convertToDto() {
        User user = mock(User.class);
        UserInfo info  = mock(UserInfo.class);
        when(mapper.map(user, UserInfo.class)).thenReturn(info);

        UserInfo expected =  convector.convertToDto(user);

        assertEquals(expected, info);
    }
}