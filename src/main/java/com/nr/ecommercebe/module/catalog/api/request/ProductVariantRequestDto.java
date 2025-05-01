package com.nr.ecommercebe.module.catalog.api.request;

import com.nr.ecommercebe.module.catalog.api.validator.ValidEnum;
import com.nr.ecommercebe.module.catalog.model.enums.ProductVariantUnit;
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
    @NotNull(message = "Variant name is required")
    String name;

    @NotNull(message = "Weight is required")
    BigDecimal weight;

    @ValidEnum(enumClass = ProductVariantUnit.class, message = "Invalid unit")
    @NotNull(message = "Unit is required")
    ProductVariantUnit unit;

    @NotNull(message = "Price is required")
    BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    Integer stockQuantity;
}
