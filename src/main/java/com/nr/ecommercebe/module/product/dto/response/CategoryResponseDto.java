package com.nr.ecommercebe.module.product.dto.response;

public record CategoryResponseDto(
    String id,
    String name,
    String imageUrl,
    String parentId
) {
}
