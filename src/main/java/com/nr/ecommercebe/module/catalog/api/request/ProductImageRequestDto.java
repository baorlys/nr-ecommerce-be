package com.nr.ecommercebe.module.catalog.api.request;

import com.nr.ecommercebe.common.model.StorageType;
import com.nr.ecommercebe.module.catalog.api.validator.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Image url is required")
    String imageUrl;

    String altText;

    @ValidEnum(enumClass = StorageType.class, message = "Invalid storage type")
    @NotNull(message = "Storage type is required")
    StorageType storageType;

    Boolean isPrimary;

    Integer sortOrder;

}
