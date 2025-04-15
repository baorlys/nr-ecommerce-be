package com.nr.ecommercebe.module.product.dto.shared;



public record ReviewDto(
    String id,
    String productId,
    String userId,
    String userName,
    Integer rating,
    String comment
) {}
