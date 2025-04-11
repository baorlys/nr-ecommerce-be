package com.nr.ecommercebe.modules.user.repository;

import com.nr.ecommercebe.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
