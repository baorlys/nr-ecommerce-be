package com.nr.ecommercebe.module.user.api;

import com.nr.ecommercebe.module.user.api.request.RegisterRequestDto;
import com.nr.ecommercebe.module.user.api.response.UserResponseDto;
import com.nr.ecommercebe.module.user.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public UserResponseDto toDTO(User user) {
        UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        dto.setFullName((firstName + " " + lastName).trim());
        return dto;
    }

    public User toEntity(RegisterRequestDto userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}