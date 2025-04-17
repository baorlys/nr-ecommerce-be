package com.nr.ecommercebe.module.catalog.api.response;

public interface ProductImageResponseDto {
    String getId();
    String getImageUrl();
    String getAltText();
    Boolean getIsPrimary();
    Integer getSortOrder();
    String getStorageType();
}
