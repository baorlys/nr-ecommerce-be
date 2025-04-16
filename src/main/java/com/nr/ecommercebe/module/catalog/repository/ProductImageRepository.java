package com.nr.ecommercebe.module.catalog.repository;

import com.nr.ecommercebe.module.catalog.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
}
