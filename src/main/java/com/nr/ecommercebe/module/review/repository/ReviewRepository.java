package com.nr.ecommercebe.module.review.repository;

import com.nr.ecommercebe.module.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReviewRepository extends JpaRepository<Review, String>, JpaSpecificationExecutor<Review> {
    boolean existsByProductIdAndUserId(String productId, String userId);
}
