package com.nr.ecommercebe.module.catalog.api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

    private List<String> acceptedValues;

    @Override
    public void initialize(ValidEnum annotation) {
        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        return value == null || acceptedValues.contains(value.name());
    }
}