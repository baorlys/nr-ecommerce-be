package com.nr.ecommercebe.module.catalog.application.domain.enums.field;

public enum ProductImageField {
    IS_PRIMARY("isPrimary"),
    IMAGE_URL("imageUrl"),;


    private final String value;

    ProductImageField(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
