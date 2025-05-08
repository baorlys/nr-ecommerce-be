package com.nr.ecommercebe.module.catalog.infrastructure.repository.custom.product;

import com.nr.ecommercebe.module.catalog.application.domain.enums.field.CategoryField;
import com.nr.ecommercebe.module.catalog.application.domain.enums.field.ProductField;
import com.nr.ecommercebe.module.catalog.application.domain.enums.field.ProductVariantField;
import com.nr.ecommercebe.module.catalog.application.dto.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.application.domain.Product;
import com.nr.ecommercebe.shared.domain.enums.BaseEntityField;
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
        addCategoryPredicate(productFilter, root, predicates);
        addCategorySlugPredicate(productFilter, root, predicates);
        addPricePredicates(productFilter, root, cb, predicates);
        addIsFeaturedPredicate(productFilter, root, cb, predicates);

        return predicates;
    }

    private static void addSearchPredicate(ProductFilter filter,
                                           Root<Product> root,
                                           CriteriaBuilder cb,
                                           List<Predicate> predicates) {
        Optional.ofNullable(filter.getSearch())
                .filter(search -> !search.trim().isEmpty()) // Ensure search is not empty
                .ifPresent(search ->
                        predicates.add(cb.or(
                                cb.like(cb.lower(root.get(ProductField.NAME.toString())), "%" +
                                        search.toLowerCase() + "%"),
                                cb.like(cb.lower(root.get(ProductField.SLUG.toString())), "%" +
                                        search.toLowerCase().replace(" ", "-") + "%"))));
    }

    // Category predicate (filter by category ID)
    private static void addCategoryPredicate(ProductFilter filter,
                                             Root<Product> root,
                                             List<Predicate> predicates) {
        Optional.ofNullable(filter.getCategoryId())
                .ifPresent(categoryId ->
                        predicates.add(root.get(ProductField.CATEGORY.toString())
                                .get(BaseEntityField.ID.toString()).in(filter.getCategoryIds())));
    }

    // Category Slug predicate (filter by category slug)
    private static void addCategorySlugPredicate(ProductFilter filter,
                                                 Root<Product> root,
                                                 List<Predicate> predicates) {
        Optional.ofNullable(filter.getCategorySlug())
                .ifPresent(categorySlug ->
                        predicates.add(root.get(ProductField.CATEGORY.toString())
                                .get(CategoryField.SLUG.toString()).in(filter.getCategorySlugs())));
    }

    // Price predicates (filter by price range)
    private static void addPricePredicates(ProductFilter filter,
                                           Root<Product> root,
                                           CriteriaBuilder cb,
                                           List<Predicate> predicates) {
        Optional.ofNullable(filter.getMinPrice())
                .ifPresent(minPrice ->
                        predicates.add(cb.greaterThanOrEqualTo(root.get(ProductField.VARIANTS.toString())
                                .get(ProductVariantField.PRICE.toString()), minPrice)));

        Optional.ofNullable(filter.getMaxPrice())
                .ifPresent(maxPrice ->
                        predicates.add(cb.lessThanOrEqualTo(root.get(ProductField.VARIANTS.toString())
                                .get(ProductVariantField.PRICE.toString()), maxPrice)));
    }

    // Featured predicate (filter by featured status)
    private static void addIsFeaturedPredicate(ProductFilter filter,
                                               Root<Product> root,
                                               CriteriaBuilder cb,
                                               List<Predicate> predicates) {
        Optional.ofNullable(filter.getIsFeatured())
                .filter(Boolean::booleanValue)
                .ifPresent(isFeatured ->
                        predicates.add(cb.isTrue(root.get(ProductField.IS_FEATURED.toString()))));
    }



}
