package com.nr.ecommercebe.module.catalog.application.service;

import com.nr.ecommercebe.module.catalog.application.dto.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminProductResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductResponseDto;
import com.nr.ecommercebe.module.catalog.application.domain.ProductVariant;
import com.nr.ecommercebe.shared.service.BaseCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService extends BaseCrudService<String, ProductRequestDto, ProductDetailResponseDto> {
    Page<ProductResponseDto> getAll(ProductFilter filter, Pageable pageable);
    Page<AdminProductResponseDto> getAllForAdmin(ProductFilter filter, Pageable pageable);
    ProductDetailResponseDto getBySlug(String slug);
    ProductVariant getProductVariantById(String variantId);
    ProductVariant getProductVariantByIdWithLock(String variantId);
    void updateProductVariantStock(ProductVariant productVariant);
}
