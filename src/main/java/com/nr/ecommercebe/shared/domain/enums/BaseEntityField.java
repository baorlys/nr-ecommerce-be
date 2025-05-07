package com.nr.ecommercebe.shared.domain.enums;

public enum BaseEntityField {
    ID("id"),
    CREATED_ON("createdOn"),
    UPDATED_ON("updatedOn"),
    DELETED("deleted");

    private final String value;

    BaseEntityField(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
