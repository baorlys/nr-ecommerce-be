package com.nr.ecommercebe.module.catalog.repository;

import com.nr.ecommercebe.module.catalog.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
}
