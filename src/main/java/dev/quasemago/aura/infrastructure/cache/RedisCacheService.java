package dev.quasemago.aura.infrastructure.cache;

import dev.quasemago.aura.config.AuraProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

@Service
public class RedisCacheService {
    private static final Logger log = LoggerFactory.getLogger(RedisCacheService.class);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final AuraProperties properties;

    public RedisCacheService(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            AuraProperties properties
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    public <T> Optional<T> get(String key, Class<T> type) {
        try {
            var value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(value, type));
        } catch (Exception error) {
            log.warn("Could not read key {} from Redis cache", key, error);
            return Optional.empty();
        }
    }

    public void set(String key, Object value) {
        try {
            var serialized = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(
                    key,
                    serialized,
                    Duration.ofSeconds(properties.getCache().getTtlSeconds())
            );
        } catch (Exception error) {
            log.warn("Could not write key {} to Redis cache", key, error);
        }
    }
}
