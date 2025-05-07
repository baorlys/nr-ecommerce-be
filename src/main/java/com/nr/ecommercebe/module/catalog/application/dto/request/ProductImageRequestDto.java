package com.nr.ecommercebe.module.catalog.application.dto.request;

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
public class ProductImageRequestDto {
    String id;

    @NotBlank(message = "Image url is required")
    String imageUrl;

    String altText;

    Boolean isPrimary;

    Integer sortOrder;
}
