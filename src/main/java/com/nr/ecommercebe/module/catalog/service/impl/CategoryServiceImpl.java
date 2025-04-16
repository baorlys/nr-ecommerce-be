package com.nr.ecommercebe.module.catalog.service.impl;

import com.nr.ecommercebe.module.catalog.api.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.CategoryResponseDto;
import com.nr.ecommercebe.module.catalog.model.Category;
import com.nr.ecommercebe.module.catalog.repository.CategoryRepository;
import com.nr.ecommercebe.module.catalog.service.CategoryService;
import com.nr.ecommercebe.shared.exception.ErrorCode;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    ModelMapper mapper;

    @Override
    public CategoryResponseDto create(CategoryRequestDto request) {
        Category category = categoryRepository.save(mapper.map(request, Category.class));
        return mapper.map(category, CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto update(String id, CategoryRequestDto request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(ErrorCode.CATEGORY_NOT_FOUND.getDefaultMessage(id));
                    return new RecordNotFoundException(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
                });
        mapper.map(request, category);
        category.setId(id);
        Category updatedCategory = categoryRepository.save(category);
        return mapper.map(updatedCategory, CategoryResponseDto.class);
    }

    @Override
    public void delete(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new RecordNotFoundException(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDto getById(String id) {
        return categoryRepository.findByIdWithDto(id)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.CATEGORY_NOT_FOUND.getMessage()));
    }

    @Override
    public List<CategoryResponseDto> getAll() {
        return categoryRepository.findAllWithDto();
    }
}
