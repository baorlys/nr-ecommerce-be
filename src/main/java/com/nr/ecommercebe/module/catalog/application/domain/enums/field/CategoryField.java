package com.nr.ecommercebe.module.catalog.application.domain.enums.field;

public enum CategoryField {
    NAME ("name"),
    SLUG ("slug"),
    PARENT ("parent"),
    IMAGE_URL ("imageUrl");

    private final String value;

    CategoryField(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
