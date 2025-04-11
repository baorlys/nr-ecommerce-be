package com.nr.ecommercebe.modules.product.repository;

import com.nr.ecommercebe.modules.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
}
