package com.nr.ecommercebe.module.user.application.dto.validation;

import com.nr.ecommercebe.shared.exception.ServerErrorException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class GenericPasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    private String passwordFieldName;
    private String confirmPasswordFieldName;
    private String message;


    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.passwordFieldName = constraintAnnotation.passwordField();
        this.confirmPasswordFieldName = constraintAnnotation.confirmPasswordField();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field passwordField = value.getClass().getDeclaredField(passwordFieldName);
            Field confirmPasswordField = value.getClass().getDeclaredField(confirmPasswordFieldName);

            Object password = passwordField.get(value);
            Object confirmPassword = confirmPasswordField.get(value);

            boolean valid = password != null && password.equals(confirmPassword);

            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(confirmPasswordFieldName)
                        .addConstraintViolation();
            }

            return valid;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ServerErrorException("Error accessing fields for password match validation", e);
        }
    }


}