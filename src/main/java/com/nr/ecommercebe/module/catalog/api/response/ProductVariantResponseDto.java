package com.nr.ecommercebe.module.catalog.api.response;


import java.math.BigDecimal;

public record ProductVariantResponseDto(
        String id,
        String name,
        BigDecimal price,
        Integer stockQuantity
) {
}