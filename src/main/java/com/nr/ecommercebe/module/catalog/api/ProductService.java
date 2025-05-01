package com.nr.ecommercebe.module.catalog.api;

import com.nr.ecommercebe.module.catalog.api.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.api.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.api.request.ProductImageRequestDto;
import com.nr.ecommercebe.module.catalog.api.request.ProductVariantRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.AdminProductResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductResponseDto;
import com.nr.ecommercebe.shared.service.BaseCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService extends BaseCrudService<String, ProductRequestDto, ProductDetailResponseDto> {
    Page<ProductResponseDto> getAll(ProductFilter filter, Pageable pageable);
    Page<AdminProductResponseDto> getAllForAdmin(ProductFilter filter, Pageable pageable);

    void addVariant(String id, ProductVariantRequestDto request);

    void updateVariant(String variantId, ProductVariantRequestDto request);

    void deleteVariant(String variantId);

    void addImage(String id, ProductImageRequestDto request);

    void deleteImage(String imageId);
}
