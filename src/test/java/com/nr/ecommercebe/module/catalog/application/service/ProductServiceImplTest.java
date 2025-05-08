package com.nr.ecommercebe.module.catalog.application.service;

import com.nr.ecommercebe.module.catalog.application.domain.Category;
import com.nr.ecommercebe.module.catalog.application.domain.Product;
import com.nr.ecommercebe.module.catalog.application.domain.ProductImage;
import com.nr.ecommercebe.module.catalog.application.domain.ProductVariant;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductImageRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductVariantRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductImageResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductVariantResponseDto;
import com.nr.ecommercebe.module.catalog.application.mapper.ProductMapper;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.CategoryRepository;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.ProductImageRepository;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.ProductRepository;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.ProductVariantRepository;
import com.nr.ecommercebe.module.media.application.domain.StorageType;
import com.nr.ecommercebe.module.messaging.infrastructure.ImageDeletePublisher;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductVariantRepository productVariantRepository;
    @Mock private ProductImageRepository productImageRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private ImageDeletePublisher imageDeletePublisher;
    @Mock private ProductMapper mapper;

    @InjectMocks private ProductServiceImpl productService;

    private ProductRequestDto productRequestDto;
    private Product product;
    private ProductDetailResponseDto productDetailResponseDto;
    private ProductVariant productVariant;
    private ProductImage productImage;

    @BeforeEach
    void setUp() {
        productRequestDto = ProductRequestDto.builder()
                .name("Test Product")
                .description("Test Description")
                .shortDescription("Test Short Description")
                .categoryId("cat1")
                .images(Set.of(new ProductImageRequestDto("img1", "url1", "alt1", true,"CLOUDINARY", 0)))
                .variants(Set.of(new ProductVariantRequestDto("var1", "var1Name", BigDecimal.valueOf(100.0), 10)))
                .build();

        product = new Product();
        product.setId("prod1");
        product.setCategory(new Category("cat1"));
        product.setName("Test Product");
        product.setSlug("test-product");
        product.setDescription("Test Description");
        product.setShortDescription("Test Short Description");
        product.setImages(new HashSet<>());
        product.setVariants(new HashSet<>());

        productDetailResponseDto = new ProductDetailResponseDto();
        productDetailResponseDto.setId("prod1");
        productDetailResponseDto.setName("Test Product");

        productVariant = new ProductVariant();
        productVariant.setId("var1");
        productVariant.setPrice(BigDecimal.valueOf(100.0));
        productVariant.setStockQuantity(10);

        productImage = new ProductImage();
        productImage.setId("img1");
        productImage.setImageUrl("url1");
    }

    @Test
    void createProduct_success() {
        when(mapper.toEntity(any())).thenReturn(product);
        when(productRepository.save(any())).thenReturn(product);
        when(mapper.mapImages(any(), any())).thenReturn(Set.of(productImage));
        when(mapper.mapVariants(any(), any())).thenReturn(Set.of(productVariant));
        when(mapper.toDto(any())).thenReturn(productDetailResponseDto);

        ProductDetailResponseDto result = productService.create(productRequestDto);

        assertNotNull(result);
        assertEquals("prod1", result.getId());
        verify(productRepository).save(any());
        verify(productImageRepository).saveAll(any());
        verify(productVariantRepository).saveAll(any());
        verify(mapper).toDto(product);
    }

    @Test
    void updateProduct_success() {
        when(productRepository.findById("prod1")).thenReturn(Optional.of(product));
        when(mapper.toVariantEntity(any())).thenReturn(productVariant);
        when(mapper.toImageEntity(any())).thenReturn(productImage);
        when(productVariantRepository.save(any())).thenReturn(productVariant);
        when(productImageRepository.save(any())).thenReturn(productImage);
        when(productRepository.save(any())).thenReturn(product);
        when(mapper.toDto(any())).thenReturn(productDetailResponseDto);

        ProductDetailResponseDto result = productService.update("prod1", productRequestDto);

        assertNotNull(result);
        assertEquals("prod1", result.getId());
        verify(productRepository).save(product);
        verify(productVariantRepository).save(any());
        verify(productImageRepository).save(any());
    }

    @Test
    void updateProduct_notFound() {
        when(productRepository.findById("prod1")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> productService.update("prod1", productRequestDto));

        verify(productRepository).findById("prod1");
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_success() {
        product.setImages(Set.of(productImage));
        product.setVariants(Set.of(productVariant));

        when(productRepository.findById("prod1")).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);

        productService.delete("prod1");

        verify(productImageRepository).delete(productImage);
        verify(imageDeletePublisher).publish("url1");
        verify(productVariantRepository).save(productVariant);
        verify(productRepository).save(product);
        assertTrue(product.isDeleted());
        assertTrue(productVariant.isDeleted());
    }

    @Test
    void getById_success() {
        when(productRepository.findByIdWithDto("prod1")).thenReturn(Optional.of(productDetailResponseDto));
        when(productVariantRepository.findByProductIdAndDeletedFalse("prod1"))
                .thenReturn(List.of(new ProductVariantResponseDto("var1", "var1Name", BigDecimal.valueOf(100.0), 10)));
        when(productImageRepository.findByProductIdAndDeletedFalse("prod1"))
                .thenReturn(List.of(new ProductImageResponseDto("img1", "url1", "alt1", true, 0, StorageType.CLOUDINARY)));

        ProductDetailResponseDto result = productService.getById("prod1");

        assertNotNull(result);
        assertEquals("prod1", result.getId());
        verify(productRepository).findByIdWithDto("prod1");
        verify(productVariantRepository).findByProductIdAndDeletedFalse("prod1");
        verify(productImageRepository).findByProductIdAndDeletedFalse("prod1");
    }

    @Test
    void getById_notFound() {
        when(productRepository.findByIdWithDto("prod1")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> productService.getById("prod1"));
        verify(productRepository).findByIdWithDto("prod1");
    }

    @Test
    void getAll_success() {
        Pageable pageable = PageRequest.of(0, 10);
        ProductFilter filter = ProductFilter.builder().categoryId("cat1").build();
        Page<ProductResponseDto> page = new PageImpl<>(Collections.emptyList());

        when(productRepository.findAllAndFilterWithDto(any(), eq(pageable))).thenReturn(page);

        Page<ProductResponseDto> result = productService.getAll(filter, pageable);

        assertNotNull(result);
        verify(productRepository).findAllAndFilterWithDto(any(), eq(pageable));
    }

    @Test
    void getBySlug_success() {
        when(productRepository.findBySlugWithDto("test-product")).thenReturn(Optional.of(productDetailResponseDto));
        when(productVariantRepository.findByProductIdAndDeletedFalse("prod1"))
                .thenReturn(List.of(new ProductVariantResponseDto("var1", "var1Name", BigDecimal.valueOf(100.0), 10)));
        when(productImageRepository.findByProductIdAndDeletedFalse("prod1"))
                .thenReturn(List.of(new ProductImageResponseDto("img1", "url1", "alt1", true, 0, StorageType.CLOUDINARY)));

        ProductDetailResponseDto result = productService.getBySlug("test-product");

        assertNotNull(result);
        assertEquals("prod1", result.getId());
        verify(productRepository).findBySlugWithDto("test-product");
    }

    @Test
    void getProductVariantById_success() {
        when(productVariantRepository.findByIdAndDeletedFalse("var1")).thenReturn(Optional.of(productVariant));

        ProductVariant result = productService.getProductVariantById("var1");

        assertNotNull(result);
        assertEquals("var1", result.getId());
        verify(productVariantRepository).findByIdAndDeletedFalse("var1");
    }

    @Test
    void updateProductVariantStock_success() {
        when(productVariantRepository.findById("var1")).thenReturn(Optional.of(productVariant));
        when(productVariantRepository.save(any())).thenReturn(productVariant);

        productService.updateProductVariantStock(productVariant);

        verify(productVariantRepository).save(productVariant);
        assertEquals(10, productVariant.getStockQuantity());
    }
}
