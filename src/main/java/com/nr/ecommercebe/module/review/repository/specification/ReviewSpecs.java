package com.nr.ecommercebe.module.review.repository.specification;

import com.nr.ecommercebe.module.review.model.Review;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecs {
    private ReviewSpecs() {
        // Private constructor to prevent instantiation
    }

    public static Specification<Review> hasProductId(String userId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("product").get("id"), userId);
    }

    public static Specification<Review> hasRating(Integer rating) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("rating"), rating);
    }

    public static Specification<Review> sortByRating(String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            if (sortDirection.equalsIgnoreCase("asc")) {
                query.orderBy(criteriaBuilder.asc(root.get("rating")));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get("rating")));
            }
            return criteriaBuilder.conjunction();
        };
    }
}
