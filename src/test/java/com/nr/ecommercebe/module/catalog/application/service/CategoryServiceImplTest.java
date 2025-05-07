package com.nr.ecommercebe.module.catalog.application.service;

import com.nr.ecommercebe.module.catalog.application.domain.Category;
import com.nr.ecommercebe.module.catalog.application.domain.Product;
import com.nr.ecommercebe.module.catalog.application.dto.request.CategoryFilter;
import com.nr.ecommercebe.module.catalog.application.dto.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminCategoryFlatResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryResponseDto;
import com.nr.ecommercebe.module.catalog.application.mapper.CategoryMapper;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.CategoryRepository;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.ProductRepository;
import com.nr.ecommercebe.shared.exception.ErrorCode;
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
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock private CategoryRepository categoryRepository;
    @Mock private ProductRepository productRepository;
    @Mock private CategoryMapper mapper;
    @InjectMocks private CategoryServiceImpl categoryService;

    private CategoryRequestDto categoryRequestDto;
    private Category category;
    private CategoryResponseDto categoryResponseDto;
    private Product product;
    private Category subCategory;

    @BeforeEach
    void setUp() {
        categoryRequestDto = CategoryRequestDto.builder()
                .name("Test Category")
                .description("Test Description")
                .parentId(null)
                .imageUrl("test-image.jpg")
                .build();

        category = new Category();
        category.setId("cat1");
        category.setName("Test Category");
        category.setSlug("test-category");
        category.setDescription("Test Description");
        category.setImageUrl("test-image.jpg");

        categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId("cat1");
        categoryResponseDto.setName("Test Category");
        categoryResponseDto.setSlug("test-category");

        product = new Product();
        product.setId("prod1");

        subCategory = new Category();
        subCategory.setId("subcat1");
        subCategory.setParent(category);
    }

    @Test
    void createCategory_success() {
        when(categoryRepository.existsByNameAndDeletedIsFalse("Test Category")).thenReturn(false);
        when(mapper.toEntity(any(CategoryRequestDto.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(mapper.toResponseDto(any(Category.class))).thenReturn(categoryResponseDto);

        CategoryResponseDto result = categoryService.create(categoryRequestDto);

        assertNotNull(result);
        assertEquals("cat1", result.getId());
        verify(categoryRepository).existsByNameAndDeletedIsFalse("Test Category");
        verify(categoryRepository).save(any(Category.class));
        verify(mapper).toResponseDto(category);
    }

    @Test
    void createCategory_duplicateName_throwsException() {
        when(categoryRepository.existsByNameAndDeletedIsFalse("Test Category")).thenReturn(true);

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> categoryService.create(categoryRequestDto));

        assertEquals(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS.getMessage(), exception.getMessage());
        verify(categoryRepository).existsByNameAndDeletedIsFalse("Test Category");
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_success() {
        categoryRequestDto.setParentId("parent1");
        Category parentCategory = new Category();
        parentCategory.setId("parent1");

        when(categoryRepository.findById("cat1")).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameAndIdIsNotAndDeletedIsFalse("Test Category", "cat1")).thenReturn(false);
        when(categoryRepository.findById("parent1")).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(mapper.toResponseDto(any(Category.class))).thenReturn(categoryResponseDto);

        CategoryResponseDto result = categoryService.update("cat1", categoryRequestDto);

        assertNotNull(result);
        assertEquals("cat1", result.getId());
        verify(categoryRepository).findById("cat1");
        verify(categoryRepository).existsByNameAndIdIsNotAndDeletedIsFalse("Test Category", "cat1");
        verify(categoryRepository).findById("parent1");
        verify(categoryRepository).save(category);
    }

    @Test
    void updateCategory_notFound_throwsException() {
        when(categoryRepository.findById("cat1")).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> categoryService.update("cat1", categoryRequestDto));

        assertEquals(ErrorCode.CATEGORY_NOT_FOUND.getMessage(), exception.getMessage());
        verify(categoryRepository).findById("cat1");
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_duplicateName_throwsException() {
        when(categoryRepository.findById("cat1")).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameAndIdIsNotAndDeletedIsFalse("Test Category", "cat1")).thenReturn(true);

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> categoryService.update("cat1", categoryRequestDto));

        assertEquals(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS.getMessage(), exception.getMessage());
        verify(categoryRepository).existsByNameAndIdIsNotAndDeletedIsFalse("Test Category", "cat1");
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void deleteCategory_success() {
        category.setProducts(Set.of(product));
        category.setSubCategories(List.of(subCategory));

        when(categoryRepository.findByIdAndDeletedIsFalse("cat1")).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(categoryRepository.save(any(Category.class))).thenReturn(subCategory);

        categoryService.delete("cat1");

        verify(categoryRepository).findByIdAndDeletedIsFalse("cat1");
        verify(productRepository).save(product);
        verify(categoryRepository).save(subCategory);
        verify(categoryRepository).delete(category);
        assertNull(product.getCategory());
        assertNull(subCategory.getParent());
    }

    @Test
    void deleteCategory_notFound_throwsException() {
        when(categoryRepository.findByIdAndDeletedIsFalse("cat1")).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> categoryService.delete("cat1"));

        assertEquals(ErrorCode.CATEGORY_NOT_FOUND.getMessage(), exception.getMessage());
        verify(categoryRepository).findByIdAndDeletedIsFalse("cat1");
        verify(categoryRepository, never()).delete(category);
    }

    @Test
    void getById_success() {
        when(categoryRepository.findByIdWithDto("cat1")).thenReturn(Optional.of(categoryResponseDto));

        CategoryResponseDto result = categoryService.getById("cat1");

        assertNotNull(result);
        assertEquals("cat1", result.getId());
        verify(categoryRepository).findByIdWithDto("cat1");
    }

    @Test
    void getById_notFound_throwsException() {
        when(categoryRepository.findByIdWithDto("cat1")).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class,
                () -> categoryService.getById("cat1"));

        assertEquals(ErrorCode.CATEGORY_NOT_FOUND.getMessage(), exception.getMessage());
        verify(categoryRepository).findByIdWithDto("cat1");
    }

    @Test
    void getAll_success() {
        when(categoryRepository.findAllByParentIsNullAndDeletedIsFalse()).thenReturn(List.of(category));
        when(mapper.mapCategoryWithChildren(any(Category.class))).thenReturn(categoryResponseDto);

        List<CategoryResponseDto> result = categoryService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("cat1", result.get(0).getId());
        verify(categoryRepository).findAllByParentIsNullAndDeletedIsFalse();
        verify(mapper).mapCategoryWithChildren(category);
    }

    @Test
    void getAllFlatForAdmin_success() {
        Pageable pageable = PageRequest.of(0, 10);
        CategoryFilter filter = CategoryFilter.builder().search("test").build();
        Page<Category> page = new PageImpl<>(List.of(category), pageable, 1);
        AdminCategoryFlatResponseDto adminDto = new AdminCategoryFlatResponseDto();
        adminDto.setId("cat1");

        when(categoryRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(mapper.toAdminCategoryFlatResponseDto(any(Category.class))).thenReturn(adminDto);

        Page<AdminCategoryFlatResponseDto> result = categoryService.getAllFlatForAdmin(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("cat1", result.getContent().getFirst().getId());
        verify(categoryRepository).findAll(any(Specification.class), eq(pageable));
        verify(mapper).toAdminCategoryFlatResponseDto(category);
    }
}
