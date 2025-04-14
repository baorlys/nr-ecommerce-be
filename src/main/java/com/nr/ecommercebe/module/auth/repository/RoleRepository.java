package com.nr.ecommercebe.module.auth.repository;

import com.nr.ecommercebe.module.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
