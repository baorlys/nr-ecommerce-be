package com.nr.ecommercebe.module.catalog.service;

import com.nr.ecommercebe.module.catalog.api.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.api.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductResponseDto;
import com.nr.ecommercebe.shared.service.BaseCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService extends BaseCrudService<String, ProductRequestDto, ProductDetailResponseDto> {
    Page<ProductResponseDto> getAll(ProductFilter filter, Pageable pageable);
}
