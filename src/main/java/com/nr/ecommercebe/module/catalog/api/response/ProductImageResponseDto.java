package com.nr.ecommercebe.module.catalog.api.response;

import com.nr.ecommercebe.common.model.StorageType;

public record ProductImageResponseDto(
        String id,
        String imageUrl,
        String altText,
        Boolean isPrimary,
        Integer sortOrder,
        StorageType storageType
) {
}