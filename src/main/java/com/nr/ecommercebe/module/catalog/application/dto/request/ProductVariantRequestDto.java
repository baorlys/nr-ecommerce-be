package com.nr.ecommercebe.module.catalog.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductVariantRequestDto {
    String id;

    @NotNull(message = "Variant name is required")
    String name;

    @NotNull(message = "Price is required")
    BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    Integer stockQuantity;
}
