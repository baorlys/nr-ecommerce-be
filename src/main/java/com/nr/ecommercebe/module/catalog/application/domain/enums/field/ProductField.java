package com.nr.ecommercebe.module.catalog.application.domain.enums.field;


public enum ProductField {
    NAME("name"),
    SLUG("slug"),
    DESCRIPTION("description"),
    SHORT_DESCRIPTION("shortDescription"),
    IS_FEATURED("isFeatured"),
    VARIANTS("variants"),
    IMAGES("images"),
    CATEGORY("category"),
    REVIEWS("reviews");

    private final String value;

    ProductField(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
