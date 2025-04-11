package com.nr.ecommercebe.modules.product.repository;

import com.nr.ecommercebe.modules.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStatusRepository extends JpaRepository<ProductStatus, String> {
}
