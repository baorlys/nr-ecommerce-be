package com.nr.ecommercebe.module.catalog.api.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CategoryResponseDto {
    String id;
    String name;
    String slug;
    String description;
    String imageUrl;
    String parentId;
    List<CategoryResponseDto> subCategories;
}
