package com.nr.ecommercebe.module.catalog.infrastructure.repository.custom.category;

import com.nr.ecommercebe.module.catalog.application.domain.Category;
import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Category> criteriaQuery;

    @Mock
    private Root<Category> root;

    @Mock
    private TypedQuery<Category> typedQuery;

    @Mock
    private Path<Object> path;

    @InjectMocks
    private CategoryRepositoryImpl categoryRepository;

    private Category parentCategory;
    private Category subCategory;

    @BeforeEach
    void setUp() {
        parentCategory = new Category();
        parentCategory.setId("cat1");
        parentCategory.setName("Electronics");
        parentCategory.setSlug("electronics");
        parentCategory.setDescription("Electronic products");
        parentCategory.setImageUrl("http://example.com/electronics.jpg");
        parentCategory.setParent(null);

        subCategory = new Category();
        subCategory.setId("cat2");
        subCategory.setName("Phones");
        subCategory.setSlug("phones");
        subCategory.setDescription("Smartphones");
        subCategory.setImageUrl("http://example.com/phones.jpg");
        subCategory.setParent(parentCategory);

        parentCategory.setSubCategories(Collections.singletonList(subCategory));
    }

    @Test
    void findAllWithDto_success_returnsTopLevelCategoriesWithSubcategories() {
        // Arrange
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Category.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Category.class)).thenReturn(root);
        when(criteriaBuilder.isNull(any())).thenReturn(mock(Predicate.class));
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate.class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(parentCategory));

        // Act
        List<CategoryResponseDto> result = categoryRepository.findAllWithDto();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        CategoryResponseDto parentDto = result.getFirst();
        assertEquals("cat1", parentDto.getId());
        assertEquals("Electronics", parentDto.getName());
        assertEquals("electronics", parentDto.getSlug());
        assertEquals("Electronic products", parentDto.getDescription());
        assertEquals("http://example.com/electronics.jpg", parentDto.getImageUrl());
        assertNull(parentDto.getParentId());
        assertEquals(1, parentDto.getSubCategories().size());
        CategoryResponseDto subDto = parentDto.getSubCategories().getFirst();
        assertEquals("cat2", subDto.getId());
        assertEquals("Phones", subDto.getName());
        assertEquals("phones", subDto.getSlug());
        assertEquals("Smartphones", subDto.getDescription());
        assertEquals("http://example.com/phones.jpg", subDto.getImageUrl());
        assertEquals("cat1", subDto.getParentId ());
        assertTrue(subDto.getSubCategories().isEmpty());
        verify(entityManager, times(1)).createQuery(criteriaQuery);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    void findAllWithDto_noTopLevelCategories_returnsEmptyList() {
        // Arrange
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Category.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Category.class)).thenReturn(root);
        when(criteriaBuilder.isNull(any())).thenReturn(mock(Predicate.class));
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate.class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

        // Act
        List<CategoryResponseDto> result = categoryRepository.findAllWithDto();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(entityManager, times(1)).createQuery(criteriaQuery);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    void findByIdWithDto_success_returnsCategoryWithSubcategories() {
        // Arrange
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Category.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Category.class)).thenReturn(root);
        when(criteriaBuilder.equal(any(), eq("cat1"))).thenReturn(mock(Predicate.class));
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate.class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.of(parentCategory));

        // Act
        Optional<CategoryResponseDto> result = categoryRepository.findByIdWithDto("cat1");

        // Assert
        assertTrue(result.isPresent());
        CategoryResponseDto parentDto = result.get();
        assertEquals("cat1", parentDto.getId());
        assertEquals("Electronics", parentDto.getName());
        assertEquals("electronics", parentDto.getSlug());
        assertEquals("Electronic products", parentDto.getDescription());
        assertEquals("http://example.com/electronics.jpg", parentDto.getImageUrl());
        assertNull(parentDto.getParentId());
        assertEquals(1, parentDto.getSubCategories().size());
        CategoryResponseDto subDto = parentDto.getSubCategories().getFirst();
        assertEquals("cat2", subDto.getId());
        assertEquals("Phones", subDto.getName());
        assertEquals("phones", subDto.getSlug());
        assertEquals("Smartphones", subDto.getDescription());
        assertEquals("http://example.com/phones.jpg", subDto.getImageUrl());
        assertEquals("cat1", subDto.getParentId ());
        assertTrue(subDto.getSubCategories().isEmpty());
        verify(entityManager, times(1)).createQuery(criteriaQuery);
        verify(typedQuery, times(1)).getResultStream();
    }

    @Test
    void findByIdWithDto_categoryNotFound_returnsEmptyOptional() {
        // Arrange
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Category.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Category.class)).thenReturn(root);
        when(criteriaBuilder.equal(any(), eq("cat1"))).thenReturn(mock(Predicate.class));
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate.class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultStream()).thenReturn(Stream.empty());

        // Act
        Optional<CategoryResponseDto> result = categoryRepository.findByIdWithDto("cat1");

        // Assert
        assertFalse(result.isPresent());
        verify(entityManager, times(1)).createQuery(criteriaQuery);
        verify(typedQuery, times(1)).getResultStream();
    }

}