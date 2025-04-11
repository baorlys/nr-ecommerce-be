package com.nr.ecommercebe.modules.user.repository;

import com.nr.ecommercebe.modules.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
