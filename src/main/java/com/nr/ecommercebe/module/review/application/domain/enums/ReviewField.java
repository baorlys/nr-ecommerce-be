package com.nr.ecommercebe.module.review.application.domain.enums;

public enum ReviewField {
    PRODUCT("product"),
    RATING("rating");

    private final String value;

    ReviewField(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
