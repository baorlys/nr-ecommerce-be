package com.nr.ecommercebe.module.catalog.infrastructure.repository;

import com.nr.ecommercebe.module.catalog.application.dto.response.ProductImageResponseDto;
import com.nr.ecommercebe.module.catalog.application.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
    List<ProductImageResponseDto> findByProductIdAndDeletedFalse(String id);
}
