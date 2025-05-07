package com.nr.ecommercebe.module.user.application.service.cache;

import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import com.nr.ecommercebe.module.user.application.domain.Role;
import com.nr.ecommercebe.module.user.application.domain.RoleName;
import com.nr.ecommercebe.module.user.infrastructure.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleCacheService {
    RoleRepository roleRepository;
    RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "role:";

    public String getRoleIdByName(RoleName roleName) {
        String key = PREFIX + roleName.name();

        // Try Redis
        String cached = (String) redisTemplate.opsForValue().get(key);
        if (cached != null) return cached;

        // Fallback to DB
        Role role = roleRepository.findRoleByName(roleName).orElseThrow(
                () -> new RecordNotFoundException("Role " + roleName + " not found in the database")
        );

        // Cache result
        redisTemplate.opsForValue().set(key, role.getId(), Duration.ofHours(1));
        return role.getId();
    }

    public Role getRole(RoleName roleName) {
        String roleId = getRoleIdByName(roleName);
        return new Role(roleId, roleName);
    }

}
