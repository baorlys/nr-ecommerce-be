package com.nr.ecommercebe.module.review.infrastructure.specification;

import com.nr.ecommercebe.module.review.application.domain.Review;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ReviewSpecsBuilder {
    private Specification<Review> spec;

    public ReviewSpecsBuilder() {
        this.spec = Specification.where(null);
    }

    public ReviewSpecsBuilder withProductId(String productId) {
        if (productId != null) {
            spec = spec.and(ReviewSpecs.hasProductId(productId));
        }
        return this;
    }

    public ReviewSpecsBuilder withSortBy(String sortBy, String sortDirection) {
        if (sortBy != null && sortDirection != null) {
            spec = spec.and(ReviewSpecs.sortByRating(sortDirection));
        }
        return this;
    }

    public ReviewSpecsBuilder withRatings(List<Integer> ratings) {
        if (ratings != null && !ratings.isEmpty()) {
            for (Integer rating : ratings) {
                spec = spec.or(ReviewSpecs.hasRating(rating));
            }
        }
        return this;
    }

    public Specification<Review> build() {
        return spec;
    }




}
