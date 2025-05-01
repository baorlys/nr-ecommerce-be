package com.nr.ecommercebe.module.catalog.service;

import com.nr.ecommercebe.module.catalog.api.mapper.ProductMapper;
import com.nr.ecommercebe.module.catalog.api.ProductService;
import com.nr.ecommercebe.module.catalog.api.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.api.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.api.request.ProductImageRequestDto;
import com.nr.ecommercebe.module.catalog.api.request.ProductVariantRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.*;
import com.nr.ecommercebe.module.catalog.model.Product;
import com.nr.ecommercebe.module.catalog.model.ProductImage;
import com.nr.ecommercebe.module.catalog.model.ProductVariant;
import com.nr.ecommercebe.module.catalog.repository.ProductImageRepository;
import com.nr.ecommercebe.module.catalog.repository.ProductRepository;
import com.nr.ecommercebe.module.catalog.repository.ProductVariantRepository;
import com.nr.ecommercebe.module.messaging.ImageDeletePublisher;
import com.nr.ecommercebe.shared.exception.ErrorCode;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
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
    ImageDeletePublisher imageDeletePublisher;

    ProductMapper mapper;

    @Override
    public ProductDetailResponseDto create(ProductRequestDto request) {
        Product product = mapper.toEntity(request);

        product.setProductImages(mapper.mapImages(request.getProductImages(), product));
        product.setProductVariants(mapper.mapVariants(request.getProductVariants(), product));

        Product createdProduct = productRepository.save(product);
        return mapper.toDto(createdProduct);
    }


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

        List<ProductVariantResponseDto> productVariants = productVariantRepository.findByProductId(id);
        List<ProductImageResponseDto> productImages = productImageRepository.findByProductId(id);
        prodDto.setVariants(productVariants);
        prodDto.setImages(productImages);
        return prodDto;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponseDto> getAll(ProductFilter filter, Pageable pageable) {
        return productRepository.findAllAndFilterWithDto(filter, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AdminProductResponseDto> getAllForAdmin(ProductFilter filter, Pageable pageable) {
        return productRepository.findAllAndFilterForAdminWithDto(filter, pageable);
    }

    @Override
    public void addVariant(String id, ProductVariantRequestDto request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(ErrorCode.PRODUCT_NOT_FOUND.getDefaultMessage(id));
                    return new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
                });

        ProductVariant variant = mapper.toVariantEntity(request);
        variant.setProduct(product);
        productVariantRepository.save(variant);
    }

    @Override
    public void updateVariant(String variantId, ProductVariantRequestDto request) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> {
                    log.error(ErrorCode.PRODUCT_VARIANT_NOT_FOUND.getDefaultMessage(variantId));
                    return new RecordNotFoundException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND.getMessage());
                });

        ProductVariant updatedVariant = mapper.toVariantEntity(request);
        updatedVariant.setId(variantId);
        updatedVariant.setProduct(variant.getProduct());

        productVariantRepository.save(updatedVariant);
    }

    @Override
    public void deleteVariant(String variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> {
                    log.error(ErrorCode.PRODUCT_VARIANT_NOT_FOUND.getDefaultMessage(variantId));
                    return new RecordNotFoundException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND.getMessage());
                });

        if (variant.getProduct().getProductVariants().size() <= 1) {
            throw new IllegalStateException("Cannot delete the last variant of a product");
        }

        variant.setDeleted(true);
        productVariantRepository.save(variant);

    }

    @Override
    public void addImage(String id, ProductImageRequestDto request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(ErrorCode.PRODUCT_NOT_FOUND.getDefaultMessage(id));
                    return new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
                });

        ProductImage image = mapper.toImageEntity(request);
        image.setProduct(product);
        productImageRepository.save(image);
    }

    @Override
    public void deleteImage(String imageId) {
        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> {
                    log.error(ErrorCode.PRODUCT_IMAGE_NOT_FOUND.getDefaultMessage(imageId));
                    return new RecordNotFoundException(ErrorCode.PRODUCT_IMAGE_NOT_FOUND.getMessage());
                });

        if (productImage.getProduct().getProductImages().size() <= 1) {
            throw new IllegalStateException("Cannot delete the last image of a product");
        }

        productImage.setDeleted(true);
        productImageRepository.save(productImage);

        imageDeletePublisher.publish(productImage.getImageUrl());
    }


}
