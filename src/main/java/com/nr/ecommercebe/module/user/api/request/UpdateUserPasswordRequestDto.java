package com.nr.ecommercebe.module.user.api.request;

import com.nr.ecommercebe.module.user.api.validation.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PUBLIC)
@PasswordMatch(passwordField = "newPassword", confirmPasswordField = "confirmPassword")
public class UpdateUserPasswordRequestDto {
    @NotBlank(message = "Password is required")
    String currentPassword;
    @Size(min = 6, message = "Password should be at least 6 characters")
    String newPassword;
    @NotBlank(message = "Confirm password is required")
    String confirmPassword;
}
