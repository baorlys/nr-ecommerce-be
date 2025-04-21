package com.nr.ecommercebe.module.user.api.validation;

import com.nr.ecommercebe.module.user.api.request.RegisterRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RegisterRequestDto> {
    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(RegisterRequestDto value, ConstraintValidatorContext context) {
        if (value.getPassword() == null || !value.getPassword().equals(value.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}