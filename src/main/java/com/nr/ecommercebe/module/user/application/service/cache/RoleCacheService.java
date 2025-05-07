package com.nr.ecommercebe.module.user.application.service.cache;

import com.nr.ecommercebe.shared.exception.ErrorCode;
import com.nr.ecommercebe.shared.exception.RecordNotFoundException;
import com.nr.ecommercebe.module.user.application.domain.Role;
import com.nr.ecommercebe.module.user.application.domain.RoleName;
import com.nr.ecommercebe.module.user.infrastructure.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
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
        Role role = roleRepository.findRoleByName(roleName)
                .orElseThrow(() -> {
                    log.error(ErrorCode.ROLE_NOT_FOUND.getSystemMessage(), roleName);
                    return new RecordNotFoundException(ErrorCode.ROLE_NOT_FOUND.getMessage());
                });

        // Cache result
        redisTemplate.opsForValue().set(key, role.getId(), Duration.ofHours(1));
        log.info("Role {} created", roleName);
        return role.getId();
    }

    public Role getRole(RoleName roleName) {
        String roleId = getRoleIdByName(roleName);
        return new Role(roleId, roleName);
    }

}
