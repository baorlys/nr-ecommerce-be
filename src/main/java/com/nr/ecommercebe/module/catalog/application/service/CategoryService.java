package com.nr.ecommercebe.module.catalog.application.service;

import com.nr.ecommercebe.module.catalog.application.dto.request.CategoryFilter;
import com.nr.ecommercebe.module.catalog.application.dto.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.AdminCategoryFlatResponseDto;
import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryResponseDto;
import com.nr.ecommercebe.shared.service.BaseCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService extends BaseCrudService<String, CategoryRequestDto, CategoryResponseDto> {
    List<CategoryResponseDto> getAll();
    Page<AdminCategoryFlatResponseDto> getAllFlatForAdmin(CategoryFilter filter, Pageable pageable);
}
