package com.nr.ecommercebe.module.catalog.api.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilter {
    String search;
    String categoryId;
    String categorySlug;
    List<String> categorySlugs;
    List<String> categoryIds;
    Double minPrice;
    Double maxPrice;
    Boolean isFeatured;
    ProductSortOption sortBy;
}
