package com.nr.ecommercebe.module.catalog.infrastructure.specification;

import com.nr.ecommercebe.module.catalog.application.domain.Category;
import com.nr.ecommercebe.module.catalog.application.domain.enums.field.CategoryField;
import com.nr.ecommercebe.shared.domain.enums.BaseEntityField;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecs {
    private CategorySpecs() {
        // Private constructor to prevent instantiation
    }

    public static Specification<Category> isDeletedFalse() {
        return (root, query, cb)
                -> cb.isFalse(root.get(BaseEntityField.DELETED.toString()));
    }

    public static Specification<Category> searchWith(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return cb.conjunction(); // No filter applied
            }
            return cb.or(
                    cb.like(cb.lower(root.get(CategoryField.NAME.toString())), "%" +
                            searchTerm.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get(CategoryField.SLUG.toString())), "%" +
                            searchTerm.toLowerCase().replace(" ","-") + "%"));
        };
    }


}
