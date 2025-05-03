package com.nr.ecommercebe.module.user.initializer;

import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import com.nr.ecommercebe.module.user.model.Role;
import com.nr.ecommercebe.module.user.model.RoleName;
import com.nr.ecommercebe.module.user.repository.RoleRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class RoleCache {
    private final RoleRepository roleRepository;
    private final Role userRole;
    private final Role adminRole;

    public RoleCache(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.userRole = roleRepository.findRoleByName(RoleName.USER).orElseThrow(
                () -> new RecordNotFoundException("Role USER not found in the database")
        );
        this.adminRole = roleRepository.findRoleByName(RoleName.ADMIN).orElseThrow(
                () -> new RecordNotFoundException("Role ADMIN not found in the database")
        );
    }

    public Role getRoleByName(RoleName roleName) {
        return switch (roleName) {
            case USER -> userRole;
            case ADMIN -> adminRole;
        };
    }

}
