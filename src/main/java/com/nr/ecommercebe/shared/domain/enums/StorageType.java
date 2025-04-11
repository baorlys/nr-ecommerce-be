package com.nr.ecommercebe.shared.domain.enums;

public enum StorageType {
    LOCAL("local"),
    CLOUDINARY("cloudinary");

    private final String value;

    StorageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static StorageType fromValue(String value) {
        for (StorageType type : StorageType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown storage type: " + value);
    }
}
