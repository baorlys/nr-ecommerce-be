package com.nr.ecommercebe.module.catalog.api.response;

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
public class CategoryFlatResponseDto {
    String id;
    String name;
    String slug;
    String description;
    String imageUrl;
    String parentId;
}
