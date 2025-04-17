package com.nr.ecommercebe.module.catalog.repository;

import com.nr.ecommercebe.module.catalog.api.response.ProductImageResponseDto;
import com.nr.ecommercebe.module.catalog.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
    List<ProductImageResponseDto> findByProductId(String id);
}
