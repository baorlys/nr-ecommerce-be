package com.nr.ecommercebe.module.catalog.application.dto.response;

import com.nr.ecommercebe.module.media.application.domain.StorageType;

public record ProductImageResponseDto(
        String id,
        String imageUrl,
        String altText,
        Boolean isPrimary,
        Integer sortOrder,
        StorageType storageType
) {
}