package com.dm.rest.util;

import com.dm.rest.payload.response.UserInfo;
import com.dm.rest.persistance.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserConvector {
    private final ModelMapper mapper;

    public UserInfo convertToDto(User user){
        return mapper.map(user, UserInfo.class);
    }

    public List<UserInfo> convertAllToDto(List<User> users){
        return users.stream()
                .map(this::convertToDto)
                .toList();
    }
}
