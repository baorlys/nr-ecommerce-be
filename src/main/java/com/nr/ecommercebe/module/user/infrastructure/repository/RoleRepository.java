package com.nr.ecommercebe.module.user.infrastructure.repository;

import com.nr.ecommercebe.module.user.application.domain.Role;
import com.nr.ecommercebe.module.user.application.domain.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByName(RoleName name);
}
