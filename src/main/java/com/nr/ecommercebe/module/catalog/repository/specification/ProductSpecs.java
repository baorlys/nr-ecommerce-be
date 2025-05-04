package com.nr.ecommercebe.module.catalog.repository.specification;

import com.nr.ecommercebe.module.catalog.api.request.ProductFilter;
import com.nr.ecommercebe.module.catalog.api.response.ProductResponseDto;
import com.nr.ecommercebe.module.catalog.model.Product;
import com.nr.ecommercebe.module.catalog.model.ProductImage;
import com.nr.ecommercebe.module.catalog.model.ProductVariant;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductSpecs {
    private ProductSpecs() {
        // Private constructor to prevent instantiation
    }

    public static Specification<Product> buildFilter(ProductFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            addSearchPredicate(filter, root, cb, predicates);
            addCategoryPredicate(filter, root, cb, predicates);
            addPricePredicates(filter, root, cb, predicates);
            addIsFeaturedPredicate(filter, root, cb, predicates);

            Join<Product, ProductVariant> variantJoin = root.join("productVariants", JoinType.LEFT);
            Join<Product, ProductImage> imageJoin = root.join("productImages", JoinType.LEFT);

            assert query != null;
            if (query.getResultType() == ProductResponseDto.class) {
                query.multiselect(
                        cb.construct(
                                ProductResponseDto.class,
                                root.get("id"),
                                root.get("name"),
                                imageJoin.get("imageUrl"),
                                cb.min(variantJoin.get("price"))
                        )
                ).groupBy(
                        root.get("id"),
                        root.get("name"),
                        imageJoin.get("imageUrl")
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addSearchPredicate(ProductFilter filter, Root<Product> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(filter.getSearch())
                .ifPresent(search -> predicates.add(cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%")));
    }

    private static void addCategoryPredicate(ProductFilter filter, Root<Product> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(filter.getCategoryId())
                .ifPresent(categoryId -> predicates.add(cb.equal(root.get("category").get("id"), categoryId)));
    }

    private static void addPricePredicates(ProductFilter filter, Root<Product> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(filter.getMinPrice())
                .ifPresent(minPrice -> predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice)));

        Optional.ofNullable(filter.getMaxPrice())
                .ifPresent(maxPrice -> predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice)));
    }

    private static void addIsFeaturedPredicate(ProductFilter filter, Root<Product> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional.ofNullable(filter.getIsFeatured())
                .filter(Boolean::booleanValue)
                .ifPresent(isFeatured -> predicates.add(cb.isTrue(root.get("isFeatured"))));
    }


}
