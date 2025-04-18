package com.nr.ecommercebe.module.catalog.repository;

import com.nr.ecommercebe.module.catalog.api.response.ProductVariantResponseDto;
import com.nr.ecommercebe.module.catalog.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
    List<ProductVariantResponseDto> findByProductId(String productId);
}
