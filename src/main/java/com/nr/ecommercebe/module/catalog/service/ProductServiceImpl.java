package com.nr.ecommercebe.module.catalog.service;

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
import org.modelmapper.ModelMapper;
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
//         Option use Specification
//         return productRepository.findBy(ProductSpecs.buildFilter(filter),
//         query -> query.as(ProductResponseDto.class).page(pageable));

//         Option use custom repository
        return productRepository.findAllAndFilterWithDto(filter, pageable);
    }


}
