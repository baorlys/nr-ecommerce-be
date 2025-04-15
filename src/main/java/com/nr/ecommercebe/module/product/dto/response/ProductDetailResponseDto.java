package com.nr.ecommercebe.module.product.dto.response;

import com.nr.ecommercebe.module.product.dto.shared.ProductImageDto;
import com.nr.ecommercebe.module.product.dto.shared.ProductVariantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductDetailResponseDto {
    String id;
    String name;
    String description;
    BigDecimal price;
    CategoryResponseDto category;
    List<ProductVariantDto> productVariants;
    List<ProductImageDto> productImages;
    BigDecimal averageRating;
    Integer totalReviews;
    Boolean isFeatured;
}
