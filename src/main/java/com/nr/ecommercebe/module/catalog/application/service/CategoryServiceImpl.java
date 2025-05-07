package com.nr.ecommercebe.module.catalog.application.service;
import com.nr.ecommercebe.module.catalog.application.dto.request.CategoryFilter;
import com.nr.ecommercebe.module.catalog.infrastructure.specification.CategorySpecs;
import com.nr.ecommercebe.shared.util.SlugUtil;
import com.nr.ecommercebe.module.catalog.application.mapper.CategoryMapper;
import com.nr.ecommercebe.module.catalog.application.dto.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminCategoryFlatResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryResponseDto;
import com.nr.ecommercebe.module.catalog.application.domain.Category;
import com.nr.ecommercebe.module.catalog.application.domain.Product;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.CategoryRepository;
import com.nr.ecommercebe.shared.exception.ErrorCode;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    CategoryMapper mapper;

    @Override
    public CategoryResponseDto create(CategoryRequestDto request) {
        if (categoryRepository.existsByNameAndDeletedIsFalse((request.getName()))) {
            throw new RecordNotFoundException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS.getMessage());
        }
        Category category = mapper.toEntity(request);
        category.setSlug(SlugUtil.generateSlug(category.getName()));

        Category savedCategory = categoryRepository.save(category);
        return mapper.toResponseDto(savedCategory);
    }

    @Override
    public CategoryResponseDto update(String id, CategoryRequestDto request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(ErrorCode.CATEGORY_NOT_FOUND.getDefaultMessage(id));
                    return new RecordNotFoundException(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
                });
        if (categoryRepository.existsByNameAndIdIsNotAndDeletedIsFalse(request.getName(), id)) {
            throw new RecordNotFoundException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS.getMessage());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSlug(SlugUtil.generateSlug(request.getName()));
        category.setParent(request.getParentId() != null ? categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.CATEGORY_NOT_FOUND.getMessage())) : null);
        category.setImageUrl(request.getImageUrl());

        Category updatedCategory = categoryRepository.save(category);

        return mapper.toResponseDto(updatedCategory);

    }

    @Override
    public void delete(String id) {
        Category category = categoryRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> {
                    log.error(ErrorCode.CATEGORY_NOT_FOUND.getDefaultMessage(id));
                    return new RecordNotFoundException(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
                });

        Set<Product> products = category.getProducts();
        products.forEach(product -> {
            product.setCategory(null);
            productRepository.save(product);
        });

        List<Category> subCategories = category.getSubCategories();
        subCategories.forEach( childCategory -> {
            childCategory.setParent(null);
            categoryRepository.save(childCategory);
        });

        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDto getById(String id) {
        return categoryRepository.findByIdWithDto(id)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.CATEGORY_NOT_FOUND.getMessage()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDto> getAll() {
        List<Category> categories = categoryRepository.findAllByParentIsNullAndDeletedIsFalse();

        return categories.stream()
                .map(mapper::mapCategoryWithChildren)
                .toList();

    }

    @Transactional(readOnly = true)
    @Override
    public Page<AdminCategoryFlatResponseDto> getAllFlatForAdmin(CategoryFilter filter, Pageable pageable) {
        Specification<Category> spec = Specification
                .where(CategorySpecs.isDeletedFalse())
                .and(CategorySpecs.searchWith(filter.getSearch()));
        Page<Category> categories = categoryRepository.findAll(spec, pageable);
        return new PageImpl<>(
                categories.getContent().stream()
                        .map(mapper::toAdminCategoryFlatResponseDto)
                        .toList(),
                pageable,
                categories.getTotalElements()
        );
    }

}
