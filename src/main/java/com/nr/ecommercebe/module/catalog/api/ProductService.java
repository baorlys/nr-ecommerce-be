package com.nr.ecommercebe.module.catalog.api;

import com.nr.ecommercebe.module.catalog.api.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.api.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.*;
import com.nr.ecommercebe.shared.service.BaseCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService extends BaseCrudService<String, ProductRequestDto, ProductDetailResponseDto> {
    Page<ProductResponseDto> getAll(ProductFilter filter, Pageable pageable);
    Page<AdminProductResponseDto> getAllForAdmin(ProductFilter filter, Pageable pageable);
    ProductDetailResponseDto getBySlug(String slug);
}
