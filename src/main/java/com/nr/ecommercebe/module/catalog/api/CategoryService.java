package com.nr.ecommercebe.module.catalog.api;

import com.nr.ecommercebe.module.catalog.api.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.CategoryResponseDto;
import com.nr.ecommercebe.common.service.BaseCrudService;

import java.util.List;

public interface CategoryService extends BaseCrudService<String, CategoryRequestDto, CategoryResponseDto> {
    List<CategoryResponseDto> getAll();
}
