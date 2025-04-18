package com.nr.ecommercebe.module.catalog.repository.custom.category;

import com.nr.ecommercebe.module.catalog.api.response.CategoryResponseDto;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryCustom {
    List<CategoryResponseDto> findAllWithDto();
    Optional<CategoryResponseDto> findByIdWithDto(String id);
}
