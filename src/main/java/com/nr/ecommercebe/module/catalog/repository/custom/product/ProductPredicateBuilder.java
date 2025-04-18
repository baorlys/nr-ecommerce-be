package com.nr.ecommercebe.module.catalog.repository.custom.product;

import com.nr.ecommercebe.module.catalog.api.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductPredicateBuilder {
    private final ProductFilter productFilter;

    public ProductPredicateBuilder(ProductFilter productFilter) {
        this.productFilter = productFilter;
    }

    public List<Predicate> build(CriteriaBuilder cb, Root<Product> root) {
        List<Predicate> predicates = new ArrayList<>();

        addSearchPredicate(productFilter, root, cb, predicates);
        addCategoryPredicate(productFilter, root, cb, predicates);
        addPricePredicates(productFilter, root, cb, predicates);
        addIsFeaturedPredicate(productFilter, root, cb, predicates);

        return predicates;
    }

    private static void addSearchPredicate(ProductFilter filter, Root<Product> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(filter.getSearch())
                .filter(search -> !search.trim().isEmpty()) // Ensure search is not empty
                .ifPresent(search -> predicates.add(cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%")));
    }

    // Category predicate (filter by category ID)
    private static void addCategoryPredicate(ProductFilter filter, Root<Product> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(filter.getCategoryId())
                .ifPresent(categoryId -> predicates.add(cb.equal(root.get("category").get("id"), categoryId)));
    }

    // Price predicates (filter by price range)
    private static void addPricePredicates(ProductFilter filter, Root<Product> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(filter.getMinPrice())
                .ifPresent(minPrice -> predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice)));

        Optional.ofNullable(filter.getMaxPrice())
                .ifPresent(maxPrice -> predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice)));
    }

    // Featured predicate (filter by featured status)
    private static void addIsFeaturedPredicate(ProductFilter filter, Root<Product> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(filter.getIsFeatured())
                .filter(Boolean::booleanValue)
                .ifPresent(isFeatured -> predicates.add(cb.isTrue(root.get("isFeatured"))));
    }



}
