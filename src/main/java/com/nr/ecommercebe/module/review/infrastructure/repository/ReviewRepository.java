package com.nr.ecommercebe.module.review.infrastructure.repository;

import com.nr.ecommercebe.module.review.application.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReviewRepository extends JpaRepository<Review, String>, JpaSpecificationExecutor<Review> {
    boolean existsByProductIdAndUserId(String productId, String userId);
}
