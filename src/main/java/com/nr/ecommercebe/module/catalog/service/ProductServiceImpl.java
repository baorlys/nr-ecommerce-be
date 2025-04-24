package com.nr.ecommercebe.module.catalog.service;

import com.nr.ecommercebe.module.catalog.api.ProductMapper;
import com.nr.ecommercebe.module.catalog.api.ProductService;
import com.nr.ecommercebe.module.catalog.api.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.api.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductImageResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.ProductVariantResponseDto;
import com.nr.ecommercebe.module.catalog.model.Product;
import com.nr.ecommercebe.module.catalog.repository.ProductImageRepository;
import com.nr.ecommercebe.module.catalog.repository.ProductRepository;
import com.nr.ecommercebe.module.catalog.repository.ProductVariantRepository;
import com.nr.ecommercebe.common.exception.ErrorCode;
import com.nr.ecommercebe.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProductVariantRepository productVariantRepository;
    ProductImageRepository productImageRepository;

    ProductMapper mapper;

    @Override
    public ProductDetailResponseDto create(ProductRequestDto request) {
        Product product = mapper.toEntity(request);

        product.setProductImages(mapper.mapImages(request.getProductImages(), product));
        product.setProductVariants(mapper.mapVariants(request.getProductVariants(), product));

        Product createdProduct = productRepository.save(product);
        return mapper.toDto(createdProduct);
    }


    // Fixme: Fix this method
    @Override
    public ProductDetailResponseDto update(String id, ProductRequestDto request) {
        productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(ErrorCode.PRODUCT_NOT_FOUND.getDefaultMessage(id));
                    return new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
                });

        Product updatedProduct = mapper.toEntity(request);
        updatedProduct.setId(id);
        Product saved = productRepository.save(updatedProduct);
        return mapper.toDto(saved);
    }



    @Override
    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDetailResponseDto getById(String id) {
        ProductDetailResponseDto prodDto = productRepository.findByIdWithDto(id)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
        List<ProductVariantResponseDto> productVariants = productVariantRepository.findByProductId(prodDto.getId());
        List<ProductImageResponseDto> productImages = productImageRepository.findByProductId(prodDto.getId());
        prodDto.setProductVariants(productVariants);
        prodDto.setProductImages(productImages);
        return prodDto;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponseDto> getAll(ProductFilter filter, Pageable pageable) {
        return productRepository.findAllAndFilterWithDto(filter, pageable);
    }


}
