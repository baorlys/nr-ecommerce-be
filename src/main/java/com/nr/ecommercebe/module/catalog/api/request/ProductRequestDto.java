package com.nr.ecommercebe.module.catalog.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductRequestDto {
    @NotBlank(message = "Product name is required")
    String name;

    String description;

    String shortDescription;
    @NotBlank(message = "Product category is required")

    String categoryId;
    @NotNull
    Set<ProductImageRequestDto> images;

    @NotNull
    Set<ProductVariantRequestDto> variants;

    Boolean isFeatured;
}
