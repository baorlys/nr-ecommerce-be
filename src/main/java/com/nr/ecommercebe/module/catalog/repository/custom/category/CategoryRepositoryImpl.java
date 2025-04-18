package com.nr.ecommercebe.module.catalog.repository.custom.category;

import com.nr.ecommercebe.module.catalog.api.response.CategoryResponseDto;
import com.nr.ecommercebe.module.catalog.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<CategoryResponseDto> findAllWithDto() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<CategoryResponseDto> query = cb.createQuery(CategoryResponseDto.class);
        Root<Category> root = query.from(Category.class);

        query
                .select(
                        cb.construct(
                                CategoryResponseDto.class,
                                root.get("id"),
                                root.get("name"),
                                root.get("imageUrl"),
                                root.get("parent").get("id")
                        )
                );

        TypedQuery<CategoryResponseDto> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<CategoryResponseDto> findByIdWithDto(String id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<CategoryResponseDto> query = cb.createQuery(CategoryResponseDto.class);
        Root<Category> root = query.from(Category.class);

        query
                .select(
                        cb.construct(
                            CategoryResponseDto.class,
                            root.get("id"),
                            root.get("name"),
                            root.get("imageUrl"),
                            root.get("parent").get("id")
                        )
                )
                .where(
                        cb.equal(root.get("id"), id)
                );

        TypedQuery<CategoryResponseDto> typedQuery = entityManager.createQuery(query);
        List<CategoryResponseDto> resultList = typedQuery.getResultList();

        return Optional.ofNullable(resultList.getFirst());
    }
}
