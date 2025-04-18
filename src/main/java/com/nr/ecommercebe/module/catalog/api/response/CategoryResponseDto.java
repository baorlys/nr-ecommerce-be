package com.nr.ecommercebe.module.catalog.api.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CategoryResponseDto {
    String id;
    String name;
    String imageUrl;
    String parentId;
}
