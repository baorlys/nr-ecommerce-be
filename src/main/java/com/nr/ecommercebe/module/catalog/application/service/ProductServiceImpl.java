package com.nr.ecommercebe.module.catalog.application.service;

import com.nr.ecommercebe.module.catalog.application.dto.response.*;
import com.nr.ecommercebe.module.catalog.application.mapper.ProductMapper;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductImageRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductVariantRequestDto;
import com.nr.ecommercebe.module.catalog.application.domain.Product;
import com.nr.ecommercebe.module.catalog.application.domain.ProductImage;
import com.nr.ecommercebe.module.catalog.application.domain.ProductVariant;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.CategoryRepository;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.ProductImageRepository;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.ProductRepository;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.ProductVariantRepository;
import com.nr.ecommercebe.module.messaging.infrastructure.ImageDeletePublisher;
import com.nr.ecommercebe.shared.exception.ErrorCode;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import com.nr.ecommercebe.shared.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProductVariantRepository productVariantRepository;
    ProductImageRepository productImageRepository;
    CategoryRepository categoryRepository;
    ImageDeletePublisher imageDeletePublisher;


    ProductMapper mapper;


    @Override
    public ProductDetailResponseDto create(ProductRequestDto request) {
        Product product = mapper.toEntity(request);
        product.setSlug(SlugUtil.generateSlug(request.getName()));

        Product createdProduct = productRepository.save(product);
        log.info("Product created with ID: {} at {}", createdProduct.getId(), LocalDateTime.now());

        Set<ProductImage> productImages = mapper.mapImages(request.getImages(), createdProduct);
        Set<ProductVariant> productVariants = mapper.mapVariants(request.getVariants(), createdProduct);

        productImageRepository.saveAll(productImages);
        log.info("Product images created for product id: {} at {}", createdProduct.getId(), LocalDateTime.now());
        productVariantRepository.saveAll(productVariants);

        return mapper.toDto(createdProduct);
    }


    @Override
    public ProductDetailResponseDto update(String id, ProductRequestDto request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(ErrorCode.PRODUCT_NOT_FOUND.getSystemMessage(id));
                    return new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
                });

        existingProduct.setName(request.getName());
        existingProduct.setSlug(SlugUtil.generateSlug(request.getName()));
        existingProduct.setDescription(request.getDescription());
        existingProduct.setShortDescription(request.getShortDescription());
        existingProduct.getCategory().setId(request.getCategoryId());
        existingProduct.setIsFeatured(request.getIsFeatured());

        List<ProductVariantResponseDto> variantResponses = updateVariants(existingProduct, request.getVariants());
        List<ProductImageResponseDto> imageResponses = updateImages(existingProduct, request.getImages());

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated with ID: {} at {}", updatedProduct.getId(), LocalDateTime.now());

        ProductDetailResponseDto productResponse = mapper.toDto(updatedProduct);
        productResponse.setVariants(variantResponses);
        productResponse.setImages(imageResponses);

        return productResponse;
    }

    private List<ProductVariantResponseDto> updateVariants(Product product, Set<ProductVariantRequestDto> request) {
        Map<String, ProductVariant> existing = product.getVariants().stream()
                .filter(v -> !v.isDeleted())
                .collect(Collectors.toMap(ProductVariant::getId, v -> v));

        List<ProductVariantResponseDto> result = new ArrayList<>();
        Set<String> incomingIds = new HashSet<>();

        for (ProductVariantRequestDto req : request) {
            ProductVariant variant;
            variant = mapper.toVariantEntity(req);
            variant.setProduct(product);
            ProductVariant updatedOrCreatedVariant = productVariantRepository.save(variant);
            log.info("Variant updated with ID: {} at {}", updatedOrCreatedVariant.getId(), LocalDateTime.now());
            incomingIds.add(updatedOrCreatedVariant.getId());
            result.add(mapper.toVariantResponseDto(updatedOrCreatedVariant));
        }

        for (ProductVariant variant : existing.values()) {
            if (!incomingIds.contains(variant.getId())) {
                variant.setDeleted(true);
                log.info("Variant deleted with ID: {} at {}", variant.getId(), LocalDateTime.now());
                productVariantRepository.save(variant);
            }
        }

        return result;
    }

    private List<ProductImageResponseDto> updateImages(Product product, Set<ProductImageRequestDto> imageRequest) {
        Map<String, ProductImage> existing = product.getImages().stream()
                .filter(img -> !img.isDeleted())
                .collect(Collectors.toMap(ProductImage::getId, img -> img));

        List<ProductImageResponseDto> result = new ArrayList<>();
        Set<String> incomingIds = new HashSet<>();

        for (ProductImageRequestDto req : imageRequest) {
            ProductImage image = mapper.toImageEntity(req);
            image.setProduct(product);
            ProductImage updatedOrCreatedImage = productImageRepository.save(image);
            log.info("Image updated with ID: {} at {}", updatedOrCreatedImage.getId(), LocalDateTime.now());
            incomingIds.add(updatedOrCreatedImage.getId());
            result.add(mapper.toImageResponseDto(updatedOrCreatedImage));
        }

        for (ProductImage img : existing.values()) {
            if (!incomingIds.contains(img.getId())) {
                imageDeletePublisher.publish(img.getImageUrl());
                product.getImages().remove(img);
                productImageRepository.delete(img);
                log.info("Image deleted with ID: {} at {}", img.getId(), LocalDateTime.now());
            }
        }

        return result;
    }

    @Override
    public void delete(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(ErrorCode.PRODUCT_NOT_FOUND.getSystemMessage(id));
                    return new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
                });

        if (!product.getImages().isEmpty()) {
            product.getImages().forEach(image -> {
                productImageRepository.delete(image);
                imageDeletePublisher.publish(image.getImageUrl());
                log.info("Image {} publish to queue to delete ", image.getImageUrl());
            });
        }
        if (!product.getVariants().isEmpty()) {
            product.getVariants().forEach(variant -> {
                variant.setDeleted(true);
                productVariantRepository.save(variant);
            });
        }

        product.setDeleted(true);
        productRepository.save(product);
        log.info("Product deleted with ID: {} at {}", product.getId(), LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDetailResponseDto getById(String id) {
        ProductDetailResponseDto prodDto = productRepository.findByIdWithDto(id)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));

        List<ProductVariantResponseDto> productVariants = productVariantRepository.findByProductIdAndDeletedFalse(id);
        List<ProductImageResponseDto> productImages = productImageRepository.findByProductIdAndDeletedFalse(id);
        prodDto.setVariants(productVariants);
        prodDto.setImages(productImages);
        return prodDto;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponseDto> getAll(ProductFilter filter, Pageable pageable) {
        ProductFilter enrichedFilter = enrichFilterWithSubcategories(filter);
        return productRepository.findAllAndFilterWithDto(enrichedFilter, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AdminProductResponseDto> getAllForAdmin(ProductFilter filter, Pageable pageable) {
        ProductFilter enrichedFilter = enrichFilterWithSubcategories(filter);
        return productRepository.findAllAndFilterForAdminWithDto(enrichedFilter, pageable);
    }

    private ProductFilter enrichFilterWithSubcategories(ProductFilter filter) {
        List<String> categoryIds = null;
        if (filter.getCategoryId() != null) {
            List<CategoryId> subCategoryIds = categoryRepository.findByParentId(filter.getCategoryId());
            subCategoryIds.add(filter::getCategoryId);
            categoryIds = subCategoryIds.stream()
                    .map(CategoryId::getId)
                    .toList();
        }

        List<String> categorySlugs = null;
        if (filter.getCategorySlug() != null) {
            List<CategorySlug> subCategorySlugs = categoryRepository.findByParentSlug(filter.getCategorySlug());
            subCategorySlugs.add(filter::getCategorySlug);
            categorySlugs = subCategorySlugs.stream()
                    .map(CategorySlug::getSlug)
                    .toList();
        }

        return filter.toBuilder()
                .categoryIds(categoryIds)
                .categorySlugs(categorySlugs)
                .build();
    }


    @Override
    public ProductDetailResponseDto getBySlug(String slug) {
        ProductDetailResponseDto prodDto = productRepository.findBySlugWithDto(slug)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));

        List<ProductVariantResponseDto> productVariants =
                productVariantRepository.findByProductIdAndDeletedFalse(prodDto.getId());
        List<ProductImageResponseDto> productImages =
                productImageRepository.findByProductIdAndDeletedFalse(prodDto.getId());
        prodDto.setVariants(productVariants);
        prodDto.setImages(productImages);
        return prodDto;
    }

    @Override
    public ProductVariant getProductVariantById(String variantId) {
        return productVariantRepository.findByIdAndDeletedFalse(variantId)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND.getMessage()));
    }

    @Override
    public ProductVariant getProductVariantByIdWithLock(String variantId) {
        return productVariantRepository.findByIdWithLock(variantId)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND.getMessage()));
    }

    @Override
    public void updateProductVariantStock(ProductVariant productVariant) {
        ProductVariant existingVariant = productVariantRepository.findById(productVariant.getId())
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND.getMessage()));

        existingVariant.setStockQuantity(productVariant.getStockQuantity());
        productVariantRepository.save(existingVariant);
    }


}
