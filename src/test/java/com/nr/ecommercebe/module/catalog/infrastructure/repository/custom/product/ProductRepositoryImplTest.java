package com.nr.ecommercebe.module.catalog.infrastructure.repository.custom.product;

import com.nr.ecommercebe.module.catalog.application.domain.Product;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductSortOption;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminProductResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductDetailResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.ProductResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<ProductResponseDto> productResponseDtoQuery;

    @Mock
    private CriteriaQuery<AdminProductResponseDto> adminProductResponseDtoQuery;

    @Mock
    private CriteriaQuery<ProductDetailResponseDto> productDetailResponseDtoQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<Product> productRoot;

    @Mock
    private Join<Object, Object> imageJoin;

    @Mock
    private Join<Object, Object> variantJoin;

    @Mock
    private Join<Object, Object> categoryJoin;

    @Mock
    private Join<Object, Object> reviewJoin;

    @Mock
    private Path<Object> idPath, namePath, slugPath, shortDescPath, descriptionPath, isFeaturedPath, createdOnPath, updatedOnPath;

    @Mock
    private Path<Object> imageUrlPath, isPrimaryPath;

    @Mock
    private Path<Object> variantPricePath, variantDeletedPath;

    @Mock
    private Path<Object> categoryIdPath, categoryNamePath, categorySlugPath, categoryImageUrlPath, categoryParentPath;

    @Mock
    private Path<Object> reviewRatingPath, reviewIdPath;

    @Mock
    private TypedQuery<ProductResponseDto> productResponseDtoTypedQuery;

    @Mock
    private TypedQuery<AdminProductResponseDto> adminProductResponseDtoTypedQuery;

    @Mock
    private TypedQuery<ProductDetailResponseDto> productDetailResponseDtoTypedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    private Pageable pageable;
    private ProductFilter filter;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        filter = new ProductFilter();
        filter.setSortBy(ProductSortOption.NEWEST);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);

        // Setup criteria queries
        when(criteriaBuilder.createQuery(ProductResponseDto.class)).thenReturn(productResponseDtoQuery);
        when(criteriaBuilder.createQuery(AdminProductResponseDto.class)).thenReturn(adminProductResponseDtoQuery);
        when(criteriaBuilder.createQuery(ProductDetailResponseDto.class)).thenReturn(productDetailResponseDtoQuery);
        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);

        // Setup root and joins
        when(productResponseDtoQuery.from(Product.class)).thenReturn(productRoot);
        when(adminProductResponseDtoQuery.from(Product.class)).thenReturn(productRoot);
        when(productDetailResponseDtoQuery.from(Product.class)).thenReturn(productRoot);
        when(countQuery.from(Product.class)).thenReturn(productRoot);

        when(productRoot.join("images", JoinType.LEFT)).thenReturn(imageJoin);
        when(productRoot.join("variants", JoinType.LEFT)).thenReturn(variantJoin);
        when(productRoot.join("category", JoinType.LEFT)).thenReturn(categoryJoin);
        when(productRoot.join("reviews", JoinType.LEFT)).thenReturn(reviewJoin);

        // Setup paths
        setupPaths();

        // Setup typed queries
        when(entityManager.createQuery(productResponseDtoQuery)).thenReturn(productResponseDtoTypedQuery);
        when(entityManager.createQuery(adminProductResponseDtoQuery)).thenReturn(adminProductResponseDtoTypedQuery);
        when(entityManager.createQuery(productDetailResponseDtoQuery)).thenReturn(productDetailResponseDtoTypedQuery);
        when(entityManager.createQuery(countQuery)).thenReturn(countTypedQuery);
    }

    private void setupPaths() {
        // Product paths
        when(productRoot.get("id")).thenReturn(idPath);
        when(productRoot.get("name")).thenReturn(namePath);
        when(productRoot.get("slug")).thenReturn(slugPath);
        when(productRoot.get("shortDescription")).thenReturn(shortDescPath);
        when(productRoot.get("description")).thenReturn(descriptionPath);
        when(productRoot.get("isFeatured")).thenReturn(isFeaturedPath);
        when(productRoot.get("createdOn")).thenReturn(createdOnPath);
        when(productRoot.get("updatedOn")).thenReturn(updatedOnPath);

        // Image paths
        when(imageJoin.get("imageUrl")).thenReturn(imageUrlPath);
        when(imageJoin.get("isPrimary")).thenReturn(isPrimaryPath);

        // Variant paths
        when(variantJoin.get("price")).thenReturn(variantPricePath);
        when(variantJoin.get("deleted")).thenReturn(variantDeletedPath);
        when(variantJoin.get("id")).thenReturn(idPath);

        // Category paths
        when(categoryJoin.get("id")).thenReturn(categoryIdPath);
        when(categoryJoin.get("name")).thenReturn(categoryNamePath);
        when(categoryJoin.get("slug")).thenReturn(categorySlugPath);
        when(categoryJoin.get("imageUrl")).thenReturn(categoryImageUrlPath);
        when(categoryJoin.get("parent")).thenReturn(categoryParentPath);
        when(categoryParentPath.get("id")).thenReturn(idPath);

        // Review paths
        when(reviewJoin.get("rating")).thenReturn(reviewRatingPath);
        when(reviewJoin.get("id")).thenReturn(reviewIdPath);
    }

    @Test
    @DisplayName("findAllAndFilterWithDto should return Page of ProductResponseDto")
    void findAllAndFilterWithDto_ShouldReturnPageOfProductResponseDto() {
        // Given
        List<ProductResponseDto> productDtos = List.of(
                new ProductResponseDto("1", "Product 1", "product-1", "image-url-1", BigDecimal.valueOf(100.0)),
                new ProductResponseDto("2", "Product 2", "product-2", "image-url-2", BigDecimal.valueOf(200.0))
        );

        when(criteriaBuilder.isTrue(any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.isFalse(any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.min(any())).thenReturn(mock(Expression.class));
        when(criteriaBuilder.desc(any())).thenReturn(mock(Order.class));
        when(criteriaBuilder.construct(eq(ProductResponseDto.class), any(), any(), any(), any(), any()))
                .thenReturn(mock(CompoundSelection.class));

        when(productResponseDtoQuery.select(any(Selection.class))).thenReturn(productResponseDtoQuery);
        when(productResponseDtoQuery.where(any(Predicate.class))).thenReturn(productResponseDtoQuery);
        when(productResponseDtoQuery.groupBy(any(Expression.class), any(Expression.class), any(Expression.class), any(Expression.class)))
                .thenReturn(productResponseDtoQuery);

        // Fix for ambiguous orderBy method call
        Order mockedOrder = mock(Order.class);
        when(productResponseDtoQuery.orderBy(mockedOrder)).thenReturn(productResponseDtoQuery);

        when(productResponseDtoTypedQuery.setFirstResult(anyInt())).thenReturn(productResponseDtoTypedQuery);
        when(productResponseDtoTypedQuery.setMaxResults(anyInt())).thenReturn(productResponseDtoTypedQuery);
        when(productResponseDtoTypedQuery.getResultList()).thenReturn(productDtos);

        when(countTypedQuery.getSingleResult()).thenReturn(2L);

        // When
        Page<ProductResponseDto> result = productRepository.findAllAndFilterWithDto(filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Product 1", result.getContent().get(0).getName());
        assertEquals("Product 2", result.getContent().get(1).getName());

        verify(productResponseDtoTypedQuery).setFirstResult(0);
        verify(productResponseDtoTypedQuery).setMaxResults(10);
        verify(productResponseDtoTypedQuery).getResultList();
        verify(countTypedQuery).getSingleResult();
    }

    @Test
    @DisplayName("findByIdWithDto should return Optional of ProductDetailResponseDto")
    void findByIdWithDto_ShouldReturnOptionalOfProductDetailResponseDto() {
        // Given
        String productId = "1";
        ProductDetailResponseDto productDetailDto = mock(ProductDetailResponseDto.class);
        List<ProductDetailResponseDto> resultList = List.of(productDetailDto);

        when(criteriaBuilder.equal(any(), eq(productId))).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.avg(any())).thenReturn(mock(Expression.class));
        when(criteriaBuilder.countDistinct(any())).thenReturn(mock(Expression.class));
        when(criteriaBuilder.construct(eq(ProductDetailResponseDto.class), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(mock(CompoundSelection.class));

        when(productDetailResponseDtoQuery.select(any(Selection.class))).thenReturn(productDetailResponseDtoQuery);
        when(productDetailResponseDtoQuery.where(any(Predicate.class))).thenReturn(productDetailResponseDtoQuery);
        when(productDetailResponseDtoQuery.groupBy(any(Expression.class), any(Expression.class), any(Expression.class),
                any(Expression.class), any(Expression.class), any(Expression.class), any(Expression.class),
                any(Expression.class), any(Expression.class)))
                .thenReturn(productDetailResponseDtoQuery);

        when(productDetailResponseDtoTypedQuery.getResultList()).thenReturn(resultList);

        // When
        Optional<ProductDetailResponseDto> result = productRepository.findByIdWithDto(productId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(productDetailDto, result.get());
        verify(productDetailResponseDtoTypedQuery).getResultList();
    }

    @Test
    @DisplayName("findByIdWithDto should return empty Optional when no product found")
    void findByIdWithDto_ShouldReturnEmptyOptional_WhenNoProductFound() {
        // Given
        String productId = "1";
        List<ProductDetailResponseDto> emptyList = new ArrayList<>();

        when(criteriaBuilder.equal(any(), eq(productId))).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.avg(any())).thenReturn(mock(Expression.class));
        when(criteriaBuilder.countDistinct(any())).thenReturn(mock(Expression.class));
        when(criteriaBuilder.construct(eq(ProductDetailResponseDto.class), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(mock(CompoundSelection.class));

        when(productDetailResponseDtoQuery.select(any(Selection.class))).thenReturn(productDetailResponseDtoQuery);
        when(productDetailResponseDtoQuery.where(any(Predicate.class))).thenReturn(productDetailResponseDtoQuery);
        when(productDetailResponseDtoQuery.groupBy(any(Expression.class), any(Expression.class), any(Expression.class),
                any(Expression.class), any(Expression.class), any(Expression.class), any(Expression.class),
                any(Expression.class), any(Expression.class)))
                .thenReturn(productDetailResponseDtoQuery);

        when(productDetailResponseDtoTypedQuery.getResultList()).thenReturn(emptyList);

        // When
        Optional<ProductDetailResponseDto> result = productRepository.findByIdWithDto(productId);

        // Then
        assertFalse(result.isPresent());
        verify(productDetailResponseDtoTypedQuery).getResultList();
    }

    @Test
    @DisplayName("findBySlugWithDto should return Optional of ProductDetailResponseDto")
    void findBySlugWithDto_ShouldReturnOptionalOfProductDetailResponseDto() {
        // Given
        String productSlug = "product-1";
        ProductDetailResponseDto productDetailDto = mock(ProductDetailResponseDto.class);
        List<ProductDetailResponseDto> resultList = List.of(productDetailDto);

        when(criteriaBuilder.equal(any(), eq(productSlug))).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.avg(any())).thenReturn(mock(Expression.class));
        when(criteriaBuilder.countDistinct(any())).thenReturn(mock(Expression.class));
        when(criteriaBuilder.construct(eq(ProductDetailResponseDto.class), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(mock(CompoundSelection.class));

        when(productDetailResponseDtoQuery.select(any(Selection.class))).thenReturn(productDetailResponseDtoQuery);
        when(productDetailResponseDtoQuery.where(any(Predicate.class))).thenReturn(productDetailResponseDtoQuery);
        when(productDetailResponseDtoQuery.groupBy(any(Expression.class), any(Expression.class), any(Expression.class),
                any(Expression.class), any(Expression.class), any(Expression.class), any(Expression.class),
                any(Expression.class), any(Expression.class)))
                .thenReturn(productDetailResponseDtoQuery);

        when(productDetailResponseDtoTypedQuery.getResultList()).thenReturn(resultList);

        // When
        Optional<ProductDetailResponseDto> result = productRepository.findBySlugWithDto(productSlug);

        // Then
        assertTrue(result.isPresent());
        assertEquals(productDetailDto, result.get());
        verify(productDetailResponseDtoTypedQuery).getResultList();
    }

    @Test
    @DisplayName("findAllAndFilterForAdminWithDto should return Page of AdminProductResponseDto")
    void findAllAndFilterForAdminWithDto_ShouldReturnPageOfAdminProductResponseDto() {
        // Given
        List<AdminProductResponseDto> adminProductDtos = List.of(
                mock(AdminProductResponseDto.class),
                mock(AdminProductResponseDto.class)
        );

        when(criteriaBuilder.isTrue(any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.isFalse(any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.countDistinct(any())).thenReturn(mock(Expression.class));
        when(criteriaBuilder.avg(any())).thenReturn(mock(Expression.class));
        when(criteriaBuilder.desc(any())).thenReturn(mock(Order.class));
        when(criteriaBuilder.construct(eq(AdminProductResponseDto.class), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(mock(CompoundSelection.class));

        when(adminProductResponseDtoQuery.select(any(Selection.class))).thenReturn(adminProductResponseDtoQuery);
        when(adminProductResponseDtoQuery.where(any(Predicate.class))).thenReturn(adminProductResponseDtoQuery);
        when(adminProductResponseDtoQuery.groupBy(any(Expression.class), any(Expression.class), any(Expression.class),
                any(Expression.class), any(Expression.class), any(Expression.class), any(Expression.class),
                any(Expression.class), any(Expression.class)))
                .thenReturn(adminProductResponseDtoQuery);

        // Fix for ambiguous orderBy method call
        Order mockedOrder = mock(Order.class);
        when(adminProductResponseDtoQuery.orderBy(mockedOrder)).thenReturn(adminProductResponseDtoQuery);

        when(adminProductResponseDtoTypedQuery.setFirstResult(anyInt())).thenReturn(adminProductResponseDtoTypedQuery);
        when(adminProductResponseDtoTypedQuery.setMaxResults(anyInt())).thenReturn(adminProductResponseDtoTypedQuery);
        when(adminProductResponseDtoTypedQuery.getResultList()).thenReturn(adminProductDtos);

        when(countTypedQuery.getSingleResult()).thenReturn(2L);

        // When
        Page<AdminProductResponseDto> result = productRepository.findAllAndFilterForAdminWithDto(filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        verify(adminProductResponseDtoTypedQuery).setFirstResult(0);
        verify(adminProductResponseDtoTypedQuery).setMaxResults(10);
        verify(adminProductResponseDtoTypedQuery).getResultList();
        verify(countTypedQuery).getSingleResult();
    }

    @Test
    @DisplayName("Different sort options should apply correct ordering")
    void differentSortOptions_ShouldApplyCorrectOrdering() {
        // Given - Price ASC
        filter.setSortBy(ProductSortOption.PRICE_ASC);
        setupQueryMocks();
        when(criteriaBuilder.asc(any())).thenReturn(mock(Order.class));

        // When
        productRepository.findAllAndFilterWithDto(filter, pageable);

        // Then
        verify(criteriaBuilder).asc(any());
        verify(productResponseDtoQuery).orderBy(any(Order.class));

        // Given - Price DESC
        filter.setSortBy(ProductSortOption.PRICE_DESC);
        setupQueryMocks();
        when(criteriaBuilder.desc(any())).thenReturn(mock(Order.class));

        // When
        productRepository.findAllAndFilterWithDto(filter, pageable);

        // Then
        verify(criteriaBuilder, atLeastOnce()).desc(any());
        verify(productResponseDtoQuery, times(2)).orderBy(any(Order.class));
    }

    private void setupQueryMocks() {
        when(criteriaBuilder.isTrue(any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.isFalse(any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));
        when(criteriaBuilder.min(any())).thenReturn(mock(Expression.class));
        when(criteriaBuilder.construct(eq(ProductResponseDto.class), any(), any(), any(), any(), any()))
                .thenReturn(mock(CompoundSelection.class));

        when(productResponseDtoQuery.select(any(Selection.class))).thenReturn(productResponseDtoQuery);
        when(productResponseDtoQuery.where(any(Predicate.class))).thenReturn(productResponseDtoQuery);
        when(productResponseDtoQuery.groupBy(any(Expression.class), any(Expression.class), any(Expression.class), any(Expression.class)))
                .thenReturn(productResponseDtoQuery);

        // Fix for ambiguous orderBy method call
        Order mockedOrder = mock(Order.class);
        when(productResponseDtoQuery.orderBy(mockedOrder)).thenReturn(productResponseDtoQuery);

        when(productResponseDtoTypedQuery.setFirstResult(anyInt())).thenReturn(productResponseDtoTypedQuery);
        when(productResponseDtoTypedQuery.setMaxResults(anyInt())).thenReturn(productResponseDtoTypedQuery);
        when(productResponseDtoTypedQuery.getResultList()).thenReturn(new ArrayList<>());

        when(countTypedQuery.getSingleResult()).thenReturn(0L);
    }
}