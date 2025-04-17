package com.nr.ecommercebe.common.exception;

import lombok.Getter;


@Getter
public enum ErrorCode {
    PRODUCT_NOT_FOUND("product.not.found", "Product not found",
            "Product with ID %s not found"),
    CATEGORY_NOT_FOUND("category.not.found","Category not found" ,
            "Category with ID%s not found" ),
    REVIEW_NOT_FOUND("review.not.found", "Review not found" ,
            "Review with ID%s not found" );


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
