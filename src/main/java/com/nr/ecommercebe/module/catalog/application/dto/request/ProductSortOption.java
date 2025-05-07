package com.nr.ecommercebe.module.catalog.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductSortOption {
    NEWEST,
    PRICE_ASC,
    PRICE_DESC;

    @JsonCreator
    public static ProductSortOption fromString(String sortBy) {
        return ProductSortOption.valueOf(sortBy.toUpperCase());
    }

}
