package com.nr.ecommercebe.module.catalog.repository;

import com.nr.ecommercebe.module.catalog.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
}
