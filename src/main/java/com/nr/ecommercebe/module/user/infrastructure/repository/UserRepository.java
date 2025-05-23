package com.nr.ecommercebe.module.user.infrastructure.repository;

import com.nr.ecommercebe.module.user.application.domain.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(@Email String email);

    Optional<User> findUserByEmail(@Email String email);
}
