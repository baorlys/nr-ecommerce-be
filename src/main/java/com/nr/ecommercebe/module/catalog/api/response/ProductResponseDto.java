package com.nr.ecommercebe.module.catalog.api.response;

import java.math.BigDecimal;


public interface ProductResponseDto {
    String getId();
    String getName();
    String getImageUrl();
    BigDecimal getPrice();
}
