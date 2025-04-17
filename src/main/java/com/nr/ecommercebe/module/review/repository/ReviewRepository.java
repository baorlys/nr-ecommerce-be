package com.nr.ecommercebe.module.review.repository;

import com.nr.ecommercebe.module.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
}
