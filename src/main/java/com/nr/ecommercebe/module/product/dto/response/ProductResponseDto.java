package com.nr.ecommercebe.module.product.dto.response;

public record ProductResponseDto(
        String id,
        String name,
        Double price,
        String imageUrl
) {
}
