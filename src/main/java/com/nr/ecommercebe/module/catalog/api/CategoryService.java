package com.nr.ecommercebe.module.catalog.api;

import com.nr.ecommercebe.module.catalog.api.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.AdminCategoryFlatResponseDto;
import com.nr.ecommercebe.module.catalog.api.response.CategoryResponseDto;
import com.nr.ecommercebe.common.service.BaseCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService extends BaseCrudService<String, CategoryRequestDto, CategoryResponseDto> {
    List<CategoryResponseDto> getAll();
    Page<AdminCategoryFlatResponseDto> getAllFlatForAdmin(Pageable pageable);
}
