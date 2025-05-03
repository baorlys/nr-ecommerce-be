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
        return modelMapper.map(user, UserResponseDto.class);
    }

    public User toEntity(RegisterRequestDto userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}