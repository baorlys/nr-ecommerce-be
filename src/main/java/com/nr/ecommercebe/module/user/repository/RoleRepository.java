package com.nr.ecommercebe.module.user.repository;

import com.nr.ecommercebe.module.user.model.Role;
import com.nr.ecommercebe.module.user.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByName(RoleName name);
}
