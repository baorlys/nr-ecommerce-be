package com.nr.ecommercebe.module.catalog.repository;

import com.nr.ecommercebe.module.catalog.api.response.CategoryId;
import com.nr.ecommercebe.module.catalog.model.Category;
import com.nr.ecommercebe.module.catalog.repository.custom.category.CategoryRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, String>, CategoryRepositoryCustom {
    @EntityGraph(attributePaths = "products, subCategories")
    Optional<Category> findByIdAndDeletedIsFalse(String id);

    @EntityGraph(attributePaths = "subCategories")
    List<Category> findAllByParentIsNullAndDeletedIsFalse();

    Page<Category> findAllByDeletedIsFalse(Pageable pageable);

    boolean existsByNameAndDeletedIsFalse(String name);

    List<Category> findAllByParent(Category category);

    boolean existsByNameAndIdIsNotAndDeletedIsFalse(String name, String id);

    List<CategoryId> findByParentId(String parentId);
}
