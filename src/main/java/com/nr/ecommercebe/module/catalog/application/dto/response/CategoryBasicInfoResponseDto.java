package com.nr.ecommercebe.module.catalog.application.dto.response;

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
public class CategoryBasicInfoResponseDto {
    String id;
    String name;
    String slug;
    String imageUrl;
    String parentId;
}
