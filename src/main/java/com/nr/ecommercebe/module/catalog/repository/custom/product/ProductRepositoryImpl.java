package com.nr.ecommercebe.module.catalog.repository.custom.product;

import com.nr.ecommercebe.module.catalog.api.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.api.response.*;
import com.nr.ecommercebe.module.catalog.model.*;
import com.nr.ecommercebe.module.review.model.Review;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ProductResponseDto> findAllAndFilterWithDto(ProductFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<ProductResponseDto> query = cb.createQuery(ProductResponseDto.class);
        Root<Product> root = query.from(Product.class);
        Join<Product, ProductImage> imageJoin = root.join("images", JoinType.LEFT);
        Join<Product, ProductVariant> variantJoin = root.join("variants", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(cb, root, imageJoin, filter);
        query.select(cb.construct(
                        ProductResponseDto.class,
                        root.get("id"),
                        root.get("name"),
                        root.get("slug"),
                        imageJoin.get("imageUrl"),
                        cb.min(variantJoin.get("price"))
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .groupBy(
                        root.get("id"),
                        root.get("name"),
                        root.get("slug"),
                        imageJoin.get("imageUrl"));

        List<ProductResponseDto> content = getPagedResult(query, pageable);
        long total = countProducts(filter);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<ProductDetailResponseDto> findByIdWithDto(String id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductDetailResponseDto> query = cb.createQuery(ProductDetailResponseDto.class);

        Root<Product> root = query.from(Product.class);
        Join<Product, Review> reviewJoin = root.join("reviews", JoinType.LEFT);
        Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);

        query.select(cb.construct(
                        ProductDetailResponseDto.class,
                        root.get("id"),
                        root.get("name"),
                        root.get("shortDescription"),
                        root.get("description"),
                        cb.construct(
                                CategoryBasicInfoResponseDto.class,
                                categoryJoin.get("id"),
                                categoryJoin.get("name"),
                                categoryJoin.get("slug"),
                                categoryJoin.get("imageUrl"),
                                categoryJoin.get("parent").get("id")
                        ),
                        cb.avg(reviewJoin.get("rating")),
                        cb.count(reviewJoin.get("id")),
                        root.get("isFeatured")
                ))
                .where(cb.equal(root.get("id"), id))
                .groupBy(
                        root.get("id"),
                        root.get("name"),
                        root.get("description"),
                        categoryJoin.get("id"),
                        categoryJoin.get("name"),
                        categoryJoin.get("imageUrl"),
                        categoryJoin.get("parent").get("id")
                );

        List<ProductDetailResponseDto> results = entityManager.createQuery(query).getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    @Override
    public Page<AdminProductResponseDto> findAllAndFilterForAdminWithDto(ProductFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<AdminProductResponseDto> query = cb.createQuery(AdminProductResponseDto.class);
        Root<Product> root = query.from(Product.class);
        Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);
        Join<Product, ProductImage> imageJoin = root.join("images", JoinType.LEFT);
        Join<Product, ProductVariant> variantJoin = root.join("variants", JoinType.LEFT);
        Join<Product, Review> reviewJoin = root.join("reviews", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(cb, root, imageJoin, filter);
        query.select(cb.construct(
                        AdminProductResponseDto.class,
                        root.get("id"),
                        root.get("name"),
                        root.get("slug"),
                        imageJoin.get("imageUrl"),
                        categoryJoin.get("id"),
                        categoryJoin.get("name"),
                        root.get("isFeatured"),
                        cb.count(variantJoin.get("id")),
                        cb.avg(reviewJoin.get("rating")),
                        cb.count(reviewJoin.get("id")),
                        root.get("createdOn"),
                        root.get("updatedOn")
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .groupBy(
                        root.get("id"),
                        root.get("name"),
                        root.get("slug"),
                        imageJoin.get("imageUrl"),
                        categoryJoin.get("id"),
                        categoryJoin.get("name"),
                        root.get("isFeatured"),
                        root.get("createdOn"),
                        root.get("updatedOn")
                );

        List<AdminProductResponseDto> content = getPagedResult(query, pageable);
        long total = countProducts(filter);

        return new PageImpl<>(content, pageable, total);
    }

    // -------------------------- Private Utility Methods --------------------------

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Product> root,
                                            Join<Product, ProductImage> imageJoin, ProductFilter filter) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isTrue(imageJoin.get("isPrimary")));
        predicates.addAll(new ProductPredicateBuilder(filter).build(cb, root));
        return predicates;
    }

    private <T> List<T> getPagedResult(CriteriaQuery<T> query, Pageable pageable) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery.getResultList();
    }

    private long countProducts(ProductFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> root = countQuery.from(Product.class);
        Join<Product, ProductImage> imageJoin = root.join("images");

        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.isTrue(imageJoin.get("isPrimary")));
        countPredicates.addAll(new ProductPredicateBuilder(filter).build(cb, root));

        countQuery.select(cb.countDistinct(root.get("id")))
                .where(cb.and(countPredicates.toArray(new Predicate[0])));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
