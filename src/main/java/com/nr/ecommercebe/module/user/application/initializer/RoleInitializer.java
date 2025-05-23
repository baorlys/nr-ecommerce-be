package com.nr.ecommercebe.module.user.application.initializer;

import com.nr.ecommercebe.module.user.application.domain.Role;
import com.nr.ecommercebe.module.user.application.domain.RoleName;
import com.nr.ecommercebe.module.user.infrastructure.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoleInitializer {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findRoleByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(roleName)));
        }
        log.info("Roles initialized");
    }
}