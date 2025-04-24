package com.nr.ecommercebe.module.catalog.api.response;

import com.nr.ecommercebe.module.catalog.model.enums.ProductVariantUnit;

import java.math.BigDecimal;

public record ProductVariantResponseDto(
        String id,
        BigDecimal weight,
        ProductVariantUnit unit,
        BigDecimal price,
        Integer stockQuantity
) {
}