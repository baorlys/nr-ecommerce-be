package com.nr.ecommercebe.module.user.application.service.cache;


import com.nr.ecommercebe.module.user.application.domain.Role;
import com.nr.ecommercebe.module.user.application.domain.RoleName;
import com.nr.ecommercebe.module.user.infrastructure.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Getter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleCacheService {
    RoleRepository roleRepository;
    Role userRole;
    Role adminRole;

    public RoleCacheService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.userRole = roleRepository.findRoleByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("User role not found"));
        this.adminRole = roleRepository.findRoleByName(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));
    }

    public Role getRole(RoleName roleName) {
        return switch (roleName) {
            case USER -> userRole;
            case ADMIN -> adminRole;
        };
    }

}
