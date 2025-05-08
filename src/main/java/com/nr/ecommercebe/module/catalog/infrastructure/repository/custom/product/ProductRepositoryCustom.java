package com.nr.ecommercebe.module.catalog.infrastructure.repository.custom.product;

import com.nr.ecommercebe.module.catalog.application.dto.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminProductResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepositoryCustom {
    Page<ProductResponseDto> findAllAndFilterWithDto(ProductFilter filter, Pageable pageable);
    Optional<ProductDetailResponseDto> findByIdWithDto(String id);
    Page<AdminProductResponseDto> findAllAndFilterForAdminWithDto(ProductFilter filter, Pageable pageable);
    Optional<ProductDetailResponseDto> findBySlugWithDto(String slug);
}
