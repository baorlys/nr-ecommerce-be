package com.nr.ecommercebe.module.catalog.infrastructure.repository.custom.category;

import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryResponseDto;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryCustom {
    List<CategoryResponseDto> findAllWithDto();
    Optional<CategoryResponseDto> findByIdWithDto(String id);
}
