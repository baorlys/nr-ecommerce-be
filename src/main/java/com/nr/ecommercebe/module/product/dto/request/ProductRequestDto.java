package com.nr.ecommercebe.module.product.dto.request;

import com.nr.ecommercebe.module.product.entity.ProductImage;
import com.nr.ecommercebe.module.product.entity.ProductVariant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @NotBlank
    String name;
    String description;
    String categoryId;
    List<ProductImage> productImages;
    List<ProductVariant> productVariants;
    Boolean isFeatured;
}
