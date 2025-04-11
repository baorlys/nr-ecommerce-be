package com.nr.ecommercebe.modules.product.repository;

import com.nr.ecommercebe.modules.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
}
