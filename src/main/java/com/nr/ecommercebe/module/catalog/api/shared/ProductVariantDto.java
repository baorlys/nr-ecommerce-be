package com.nr.ecommercebe.module.catalog.api.shared;

import com.nr.ecommercebe.module.catalog.model.enums.ProductVariantUnit;
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
public class ProductVariantDto {
    String id;
    BigDecimal weight;
    ProductVariantUnit unit;
    BigDecimal price;
    Integer stockQuantity;
}
