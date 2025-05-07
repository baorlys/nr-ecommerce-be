package com.nr.ecommercebe.module.user.application.dto.request;

import com.nr.ecommercebe.module.user.application.dto.validation.PasswordMatch;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PUBLIC)
@PasswordMatch(passwordField = "password", confirmPasswordField = "confirmPassword")
public class RegisterRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password should be at least 6 characters")
    String password;
    @NotBlank(message = "Confirm password is required")
    String confirmPassword;
    @NotBlank(message = "First name is required")
    String firstName;
    String lastName;
}
