package com.nr.ecommercebe.module.catalog.api.response;

import com.nr.ecommercebe.module.catalog.model.enums.ProductVariantUnit;
import java.math.BigDecimal;

public interface ProductVariantResponseDto {
    String getId();
    BigDecimal getWeight();
    ProductVariantUnit getUnit();
    BigDecimal getPrice();
    Integer getStockQuantity();
}
