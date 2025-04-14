package com.nr.ecommercebe.module.auth.repository;

import com.nr.ecommercebe.module.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
