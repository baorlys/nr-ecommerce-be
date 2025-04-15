package com.nr.ecommercebe.module.product.service.impl;

import com.nr.ecommercebe.module.product.dto.request.ProductFilter;
import com.nr.ecommercebe.module.product.dto.request.ProductRequestDto;
import com.nr.ecommercebe.module.product.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.product.dto.response.ProductResponseDto;
import com.nr.ecommercebe.module.product.dto.shared.ReviewDto;
import com.nr.ecommercebe.module.product.entity.Product;
import com.nr.ecommercebe.module.product.repository.ProductRepository;
import com.nr.ecommercebe.module.product.repository.ReviewRepository;
import com.nr.ecommercebe.module.product.service.ProductService;
import com.nr.ecommercebe.shared.constant.ErrorCode;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ReviewRepository reviewRepository;
    ModelMapper mapper;

    @Override
    public ProductDetailResponseDto create(ProductRequestDto request) {
        Product createdProduct = productRepository.save(mapper.map(request, Product.class));
        return mapper.map(createdProduct, ProductDetailResponseDto.class);
    }

    @Override
    public ProductDetailResponseDto update(String id, ProductRequestDto request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(ErrorCode.PRODUCT_NOT_FOUND.getDefaultMessage(id));
                    return new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
                });
        mapper.map(request, product);
        product.setId(id);
        Product updatedProduct = productRepository.save(product);
        return mapper.map(updatedProduct, ProductDetailResponseDto.class);
    }

    @Override
    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            log.error(ErrorCode.PRODUCT_NOT_FOUND.getDefaultMessage(id));
            throw new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDetailResponseDto getById(String id) {
        return productRepository.findByIdWithProjection(id)
                .orElseThrow(() -> {
                    log.error(ErrorCode.PRODUCT_NOT_FOUND.getDefaultMessage(id));
                    return new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
                });
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductDetailResponseDto> getAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> mapper.map(product, ProductDetailResponseDto.class));
    }


    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponseDto> getAll(ProductFilter filter, Pageable pageable) {
        return productRepository.findAllAndFilterWithProjection(filter, pageable);
    }

    @Override
    public Page<ReviewDto> getProductReviews(String id) {
        return
    }
}
