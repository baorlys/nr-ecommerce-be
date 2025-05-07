package com.nr.ecommercebe.module.catalog.application.mapper;

import com.nr.ecommercebe.module.catalog.application.dto.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminCategoryFlatResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryResponseDto;
import com.nr.ecommercebe.module.catalog.application.domain.Category;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    private final ModelMapper mapper;


    public CategoryResponseDto toResponseDto(Category category) {
        return mapper.map(category, CategoryResponseDto.class);
    }

    public CategoryResponseDto mapCategoryWithChildren(Category category) {
        CategoryResponseDto dto = mapper.map(category, CategoryResponseDto.class);

        List<CategoryResponseDto> subCategoriesDto = category.getSubCategories().stream()
                .map(this::mapCategoryWithChildren)
                .toList();

        dto.setSubCategories(subCategoriesDto);
        return dto;
    }


    public Category toEntity(CategoryRequestDto dto) {
        Category category = mapper.map(dto, Category.class);
        if (dto.getParentId() != null) {
            Category parent = new Category();
            parent.setId(dto.getParentId());
            category.setParent(parent);
        }
        return category;
    }

    public AdminCategoryFlatResponseDto toAdminCategoryFlatResponseDto(Category category) {
        AdminCategoryFlatResponseDto dto = mapper.map(category, AdminCategoryFlatResponseDto.class);
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
        }
        return dto;
    }
}
