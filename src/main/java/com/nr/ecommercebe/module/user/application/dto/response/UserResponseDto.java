package com.nr.ecommercebe.module.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    String id;
    String email;
    String firstName;
    String lastName;
    String phone;
    RoleResponseDto role;
    String createdOn;
    String updatedOn;
}

