package com.nr.ecommercebe.module.catalog.api.request;

import jakarta.validation.constraints.NotBlank;
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
public class CategoryRequestDto {
    @NotBlank(message = "Category name is required")
    String name;

    String description;

    @NotBlank(message = "Category image is required")
    String imageUrl;

    String parentId;

}
