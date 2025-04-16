package com.nr.ecommercebe.module.catalog.service;

import com.nr.ecommercebe.module.catalog.api.request.CategoryRequestDto;
import com.nr.ecommercebe.module.catalog.api.response.CategoryResponseDto;
import com.nr.ecommercebe.shared.service.BaseCrudService;

import java.util.List;

public interface CategoryService extends BaseCrudService<String, CategoryRequestDto, CategoryResponseDto> {
    List<CategoryResponseDto> getAll();
}
