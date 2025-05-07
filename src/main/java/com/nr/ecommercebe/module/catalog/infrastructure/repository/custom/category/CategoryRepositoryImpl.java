package com.nr.ecommercebe.module.catalog.infrastructure.repository.custom.category;

import com.nr.ecommercebe.module.catalog.application.dto.response.CategoryResponseDto;
import com.nr.ecommercebe.module.catalog.application.domain.Category;
import com.nr.ecommercebe.shared.domain.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<CategoryResponseDto> findAllWithDto() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        query.select(root).where(cb.isNull(root.get("parent")));

        List<Category> topCategories = entityManager.createQuery(query).getResultList();

        return topCategories.stream()
                .map(this::mapCategoryWithChildren)
                .toList();
    }

    private CategoryResponseDto mapCategoryWithChildren(Category category) {
        CategoryResponseDto[] subCategoriesDto = category.getSubCategories().stream()
                .map(this::mapCategory)
                .toArray(CategoryResponseDto[]::new);

        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getImageUrl(),
                Optional.ofNullable(category.getParent())
                        .map(BaseEntity::getId)
                        .orElse(null),
                new ArrayList<>(List.of(subCategoriesDto))// No deeper nesting
        );
    }

    private CategoryResponseDto mapCategory(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getImageUrl(),
                Optional.ofNullable(category.getParent())
                        .map(Category::getId)
                        .orElse(null),
                new ArrayList<>()// No deeper nesting
        );
    }

    @Override
    public Optional<CategoryResponseDto> findByIdWithDto(String id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        query.select(root).where(cb.equal(root.get("id"), id));

        Category category = entityManager.createQuery(query).getResultStream().findFirst().orElse(null);

        if (category == null) {
            return Optional.empty();
        }

        return Optional.of(mapCategoryWithChildren(category));
    }
}
