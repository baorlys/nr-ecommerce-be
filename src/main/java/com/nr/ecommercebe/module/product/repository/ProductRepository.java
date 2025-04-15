package com.nr.ecommercebe.module.product.repository;

import com.nr.ecommercebe.module.product.entity.Product;
import com.nr.ecommercebe.module.product.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, ProductRepositoryCustom {

}
