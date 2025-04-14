package com.nr.ecommercebe.module.product.repository;

import com.nr.ecommercebe.module.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
}
