package com.nr.ecommercebe.module.catalog.api.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProductImageDto {
    String id;
    String imageUrl;
    String altText;
    Boolean isPrimary;
    Integer sortOrder;
    String storageType;
}
