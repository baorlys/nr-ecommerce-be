package com.nr.ecommercebe.module.catalog.api.shared;



public record ReviewDto(
    String id,
    String productId,
    String userId,
    String userName,
    Integer rating,
    String comment
) {}
