package com.nr.ecommercebe.module.catalog.api.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilter {
    String search;
    String categoryId;
    List<String> categoryIds;
    Double minPrice;
    Double maxPrice;
    Boolean isFeatured;
    ProductSortOption sortBy;
}
