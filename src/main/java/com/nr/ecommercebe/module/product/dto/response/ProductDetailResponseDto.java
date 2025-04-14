package com.nr.ecommercebe.module.product.dto.response;

import com.nr.ecommercebe.module.product.dto.shared.ProductImageDto;
import com.nr.ecommercebe.module.product.dto.shared.ProductVariantDto;
import com.nr.ecommercebe.module.product.dto.shared.ReviewDto;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailResponseDto(
        String id,
        String name,
        String description,
        BigDecimal price,
        CategoryResponseDto category,
        List<ProductVariantDto> productVariants,
        List<ProductImageDto> productImages,
        List<ReviewDto> reviews,
        BigDecimal averageRating,
        Integer totalReviews,
        Boolean isFeatured
) {
}
