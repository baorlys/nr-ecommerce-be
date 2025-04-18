package com.nr.ecommercebe.module.catalog.api.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductDetailResponseDto {
    String id;
    String name;
    String description;
    CategoryResponseDto category;
    List<ProductVariantResponseDto> productVariants;
    List<ProductImageResponseDto> productImages;
    Double averageRating;
    Long totalReviews;

    public ProductDetailResponseDto(String id,
                                    String name,
                                    String description,
                                    CategoryResponseDto category,
                                    Double averageRating,
                                    Long totalReviews) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
    }
}
