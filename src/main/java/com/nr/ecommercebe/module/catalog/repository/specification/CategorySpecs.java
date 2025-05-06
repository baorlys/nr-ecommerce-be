package com.nr.ecommercebe.module.catalog.repository.specification;

import com.nr.ecommercebe.module.catalog.model.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecs {
    private CategorySpecs() {
        // Private constructor to prevent instantiation
    }

    public static Specification<Category> isDeletedFalse() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }

    public static Specification<Category> searchWith(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return cb.conjunction(); // No filter applied
            }
            return cb.or(cb.like(cb.lower(root.get("name")), "%" + searchTerm.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("slug")), "%" + searchTerm.toLowerCase() + "%"));
        };
    }


}
