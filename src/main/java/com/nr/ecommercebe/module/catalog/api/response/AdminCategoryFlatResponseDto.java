package com.nr.ecommercebe.module.catalog.api.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AdminCategoryFlatResponseDto {
    String id;
    String name;
    String slug;
    String description;
    String imageUrl;
    String parentId;
    LocalDate createdOn;
    LocalDate updatedOn;
}
