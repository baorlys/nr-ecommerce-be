package com.nr.ecommercebe.module.catalog.repository;

import com.nr.ecommercebe.module.catalog.model.Category;
import com.nr.ecommercebe.module.catalog.model.Product;
import com.nr.ecommercebe.module.catalog.repository.custom.product.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, ProductRepositoryCustom, JpaSpecificationExecutor<Product> {
    List<Product> findAllByCategory(Category category);
}
