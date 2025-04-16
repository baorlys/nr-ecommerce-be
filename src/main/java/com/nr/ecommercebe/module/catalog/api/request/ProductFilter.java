package com.nr.ecommercebe.module.catalog.api.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilter {
    String search;
    String categoryId;
    Double minPrice;
    Double maxPrice;
    Boolean isFeatured;
    String sortBy;
    String sortDirection;
}
