package com.nr.ecommercebe.module.catalog.api.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductDetailResponseDto {
    String id;
    String name;
    String shortDescription;
    String description;
    CategoryBasicInfoResponseDto category;
    List<ProductVariantResponseDto> variants;
    List<ProductImageResponseDto> images;
    Double averageRating;
    Long totalReviews;
    Boolean isFeatured;

    public ProductDetailResponseDto(String id,
                                    String name,
                                    String shortDescription,
                                    String description,
                                    CategoryBasicInfoResponseDto category,
                                    Double averageRating,
                                    Long totalReviews,
                                    Boolean isFeatured) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.description = description;
        this.category = category;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.isFeatured = isFeatured;
    }
}
