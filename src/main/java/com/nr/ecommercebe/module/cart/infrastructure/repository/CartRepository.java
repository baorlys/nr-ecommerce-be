package com.nr.ecommercebe.module.cart.infrastructure.repository;

import com.nr.ecommercebe.module.cart.application.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUserId(String id);
}
