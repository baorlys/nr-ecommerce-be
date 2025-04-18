package com.nr.ecommercebe.module.catalog.repository.custom.product;

import com.nr.ecommercebe.module.catalog.api.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.api.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepositoryCustom {
    Page<ProductResponseDto> findAllAndFilterWithDto(ProductFilter filter, Pageable pageable);
    Optional<ProductDetailResponseDto> findByIdWithDto(String id);
}
