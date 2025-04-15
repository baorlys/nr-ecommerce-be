package com.nr.ecommercebe.module.product.repository.custom;

import com.nr.ecommercebe.module.product.dto.request.ProductFilter;
import com.nr.ecommercebe.module.product.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.product.dto.response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepositoryCustom {
    Page<ProductResponseDto> findAllAndFilterWithProjection(ProductFilter filter, Pageable pageable);
    Optional<ProductDetailResponseDto> findByIdWithProjection(String id);
}
