package com.nr.ecommercebe.module.catalog.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductRequestDto {
    @NotBlank(message = "Product name is required")
    String name;
    String description;
    @NotBlank(message = "Product category is required")
    String categoryId;
    List<ProductImageRequestDto> productImages;
    List<ProductVariantRequestDto> productVariants;
    Boolean isFeatured;
}
