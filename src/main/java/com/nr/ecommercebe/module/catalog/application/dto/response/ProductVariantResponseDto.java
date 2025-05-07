package com.nr.ecommercebe.module.catalog.application.dto.response;


import java.math.BigDecimal;

public record ProductVariantResponseDto(
        String id,
        String name,
        BigDecimal price,
        Integer stockQuantity
) {
}