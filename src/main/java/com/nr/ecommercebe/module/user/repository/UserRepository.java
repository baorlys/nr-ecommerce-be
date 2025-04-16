package com.nr.ecommercebe.module.user.repository;

import com.nr.ecommercebe.module.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
