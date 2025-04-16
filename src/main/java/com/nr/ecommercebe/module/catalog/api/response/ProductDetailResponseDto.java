package com.nr.ecommercebe.module.catalog.api.response;

import com.nr.ecommercebe.module.catalog.api.shared.ProductImageDto;
import com.nr.ecommercebe.module.catalog.api.shared.ProductVariantDto;
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
    List<ProductVariantDto> productVariants;
    List<ProductImageDto> productImages;
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
