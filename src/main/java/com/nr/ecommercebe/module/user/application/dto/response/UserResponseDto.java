package com.nr.ecommercebe.module.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    LocalDate createdOn;
    LocalDate updatedOn;
}

