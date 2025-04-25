package com.nr.ecommercebe.module.catalog.service;

import com.nr.ecommercebe.common.util.SlugUtil;
import com.nr.ecommercebe.module.catalog.api.CategoryMapper;
import com.nr.ecommercebe.module.catalog.api.CategoryService;
import com.nr.ecommercebe.module.catalog.api.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.CategoryResponseDto;
import com.nr.ecommercebe.module.catalog.model.Category;
import com.nr.ecommercebe.module.catalog.repository.CategoryRepository;
import com.nr.ecommercebe.common.exception.ErrorCode;
import com.nr.ecommercebe.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper mapper;

    @Override
    public CategoryResponseDto create(CategoryRequestDto request) {
        if (categoryRepository.existsByName((request.getName()))) {
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
        if (categoryRepository.existsByName(request.getName())) {
            throw new RecordNotFoundException(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS.getMessage());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSlug(SlugUtil.generateSlug(request.getName()));
        category.setParent(request.getParentId() != null ? categoryRepository.findById(request.getParentId()).orElse(null) : null);

        Category updatedCategory = categoryRepository.save(category);

        return mapper.toResponseDto(updatedCategory);

    }

    @Override
    public void delete(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new RecordNotFoundException(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
        }
        categoryRepository.deleteById(id);
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
        List<Category> categories = categoryRepository.findAllByParentIsNull();

        return categories.stream()
                .map(mapper::mapCategoryWithChildren)
                .toList();

    }

}
