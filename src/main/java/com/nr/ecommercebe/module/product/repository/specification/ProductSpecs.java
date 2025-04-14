package com.nr.ecommercebe.module.product.repository.specification;

import com.nr.ecommercebe.module.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecs {
    private ProductSpecs() {
        // Private constructor to prevent instantiation
    }

    public static Specification<Product> isFeatured() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isFeatured"), true);
    }
}
