package com.nr.ecommercebe.module.product.entity.enums;

import lombok.Getter;

@Getter
public enum ProductVariantUnit {
    KG("kg"),
    G("g"),
    L("l"),
    ML("ml");

    private final String unit;

    ProductVariantUnit(String unit) {
        this.unit = unit;
    }
}
