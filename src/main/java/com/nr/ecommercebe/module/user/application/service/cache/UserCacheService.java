package com.nr.ecommercebe.module.user.application.service.cache;

import com.nr.ecommercebe.module.user.application.dto.response.UserResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCacheService {
    RedisTemplate<String, Object> redisTemplate;
    static final String PREFIX = "user:";

    public void cacheUser(UserResponseDto user) {
        redisTemplate.opsForValue().set(PREFIX + user.getId(), user, Duration.ofMinutes(30));
    }

    public UserResponseDto getCachedUserById(String userId) {
        return (UserResponseDto) redisTemplate.opsForValue().get(PREFIX + userId);
    }

    public void evictUserById(String userId) {
        redisTemplate.delete(PREFIX + userId);
    }
}
