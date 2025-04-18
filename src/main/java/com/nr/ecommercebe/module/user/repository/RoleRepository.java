package com.nr.ecommercebe.module.user.repository;

import com.nr.ecommercebe.module.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
