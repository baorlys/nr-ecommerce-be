package com.nr.ecommercebe.module.catalog.repository;

import com.nr.ecommercebe.module.catalog.model.Category;
import com.nr.ecommercebe.module.catalog.repository.custom.category.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, String>, CategoryRepositoryCustom {

}
