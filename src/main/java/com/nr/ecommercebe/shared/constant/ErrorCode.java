package com.nr.ecommercebe.shared.constant;

import lombok.Getter;


@Getter
public enum ErrorCode {
    // Product
    PRODUCT_NOT_FOUND("product.not.found", "Product not found",
            "Product with ID %s not found");
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
