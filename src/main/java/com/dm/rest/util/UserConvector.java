package com.dm.rest.util;

import com.dm.rest.dto.UserDTO;
import com.dm.rest.persistance.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserConvector{

    private final ModelMapper modelMapper;

    public UserConvector(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO convertToDto(User entity){
        log.debug("User convector work (convert to DTO)");
        return Objects.isNull(entity) ? null : modelMapper.map(entity, UserDTO.class);
    }

    public User convertEntity(UserDTO dto){
        log.debug("User convector work (convert to DTO)");
        return Objects.isNull(dto) ? null : modelMapper.map(dto, User.class);
    }

    public List<UserDTO> convertAllEntity(List<User> users){
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
