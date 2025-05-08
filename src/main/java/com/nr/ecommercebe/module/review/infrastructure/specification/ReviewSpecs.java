package com.nr.ecommercebe.module.review.infrastructure.specification;

import com.nr.ecommercebe.module.review.application.domain.Review;
import com.nr.ecommercebe.module.review.application.domain.enums.ReviewField;
import com.nr.ecommercebe.shared.domain.enums.BaseEntityField;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecs {
    private ReviewSpecs() {
        // Private constructor to prevent instantiation
    }

    public static Specification<Review> hasProductId(String userId) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder
                .equal(root.get(ReviewField.PRODUCT.toString()).get(BaseEntityField.ID.toString()), userId);
    }

    public static Specification<Review> hasRating(Integer rating) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(ReviewField.RATING.toString()), rating);
    }

    public static Specification<Review> sortByRating(String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            if (sortDirection.equalsIgnoreCase("asc")) {
                query.orderBy(criteriaBuilder.asc(root.get(ReviewField.RATING.toString())));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(ReviewField.RATING.toString())));
            }
            return criteriaBuilder.conjunction();
        };
    }
}
