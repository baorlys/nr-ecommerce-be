package com.nr.ecommercebe.module.catalog.application.domain.enums.field;

public enum ProductVariantField {
    PRICE("price");

    private final String value;

    ProductVariantField(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
