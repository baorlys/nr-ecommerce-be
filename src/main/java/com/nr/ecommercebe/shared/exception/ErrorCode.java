package com.nr.ecommercebe.shared.exception;

import lombok.Getter;


@Getter
public enum ErrorCode {
    PRODUCT_NOT_FOUND("product.not.found", "Product not found",
            "Product with ID %s not found"),
    CATEGORY_NOT_FOUND("category.not.found","Category not found" ,
            "Category with ID%s not found" ),
    REVIEW_NOT_FOUND("review.not.found", "Review not found" ,
            "Review with ID%s not found" ),
    PRODUCT_IMAGE_NOT_FOUND("product.image.not.found", "Product image not found",
            "Product image with ID%s not found" ),
    PRODUCT_VARIANT_NOT_FOUND("product.variant.not.found", "Product variant not found",
            "Product variant with ID%s not found" ),
    USER_NOT_FOUND("user.not.found", "User not found",
            "User with ID%s not found" ),
    CATEGORY_NAME_ALREADY_EXISTS("category.name.already.exists", "Category name already exists",
            "Category name %s already exists"),
    PRODUCT_NAME_ALREADY_EXISTS("product.name.already.exists", "Product name already exists",
            "Product name %s already exists"),
    REVIEW_ALREADY_EXISTS("review.already.exists", "Review already exists",
            "Review already exists for product %s and user %s"),
    EMAIL_EXISTS("email.exists", "Email already exists",
            "Email %s already exists"),
    PHONE_EXISTS("phone.exists", "Phone already exists",
            "Phone %s already exists"),
    USER_EMAIL_NOT_FOUND("user.email.not.found", "User not found",
            "User with email %s not found"),
    INVALID_CREDENTIAL("invalid.user.password", "Invalid email or password",
            "Invalid email or password"),
    JWT_IS_INVALID("jwt.is_invalid", "JWT is invalid",
            "JWT is invalid"),
    UNAUTHORIZED("unauthorized", "Access denied. Invalid or missing token.",
            "Unauthorized access"), ;



    private final String code;
    private final String message;
    private final String defaultMessage;

    ErrorCode(String code, String message, String defaultMessage) {
        this.code = code;
        this.message = message;
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage(String ... args) {
        return String.format(defaultMessage, (Object[]) args);
    }
}
