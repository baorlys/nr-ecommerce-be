package com.nr.ecommercebe.module.product.service;

import com.nr.ecommercebe.module.product.dto.request.ProductFilter;
import com.nr.ecommercebe.module.product.dto.request.ProductRequestDto;
import com.nr.ecommercebe.module.product.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.product.dto.response.ProductResponseDto;
import com.nr.ecommercebe.module.product.dto.shared.ReviewDto;
import com.nr.ecommercebe.shared.service.BaseCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService extends BaseCrudService<String, ProductRequestDto, ProductDetailResponseDto> {
    Page<ProductResponseDto> getAll(ProductFilter filter, Pageable pageable);
    Page<ReviewDto> getProductReviews(String id);
}
