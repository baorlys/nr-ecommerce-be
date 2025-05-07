package com.nr.ecommercebe.module.catalog.infrastructure.repository;

import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryId;
import com.nr.ecommercebe.module.catalog.application.dto.response.CategorySlug;
import com.nr.ecommercebe.module.catalog.application.domain.Category;
import com.nr.ecommercebe.module.catalog.infrastructure.repository.custom.category.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, String>, CategoryRepositoryCustom, JpaSpecificationExecutor<Category> {
    @EntityGraph(attributePaths = "products, subCategories")
    Optional<Category> findByIdAndDeletedIsFalse(String id);

    @EntityGraph(attributePaths = "subCategories")
    List<Category> findAllByParentIsNullAndDeletedIsFalse();

    boolean existsByNameAndDeletedIsFalse(String name);


    boolean existsByNameAndIdIsNotAndDeletedIsFalse(String name, String id);

    List<CategoryId> findByParentId(String parentId);

    List<CategorySlug> findByParentSlug(String categorySlug);
}
