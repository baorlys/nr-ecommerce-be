package com.nr.ecommercebe.module.product.repository;

import com.nr.ecommercebe.module.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
}
