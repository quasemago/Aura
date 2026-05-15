package dev.quasemago.aura.infrastructure.jikan;

import dev.quasemago.aura.infrastructure.cache.RedisCacheService;
import dev.quasemago.aura.infrastructure.jikan.dto.Anime;
import dev.quasemago.aura.infrastructure.jikan.dto.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JikanService {
    private static final Logger log = LoggerFactory.getLogger(JikanService.class);

    private final JikanClient jikanClient;
    private final RedisCacheService redisCacheService;

    public JikanService(JikanClient jikanClient, RedisCacheService redisCacheService) {
        this.jikanClient = jikanClient;
        this.redisCacheService = redisCacheService;
    }

    public Optional<Anime> getAnimeDetails(String title, boolean sfw) {
        var cacheKey = "mal|anime:%s".formatted(title);
        var cached = redisCacheService.get(cacheKey, Anime.class);
        if (cached.isPresent()) {
            log.debug("Cache hit for anime title: {}", title);
            return cached;
        }

        var response = jikanClient.searchAnime(title, 1, sfw);
        if (response == null || response.data() == null || response.data().isEmpty()) {
            return Optional.empty();
        }

        var anime = response.data().getFirst();

        log.debug("Saving anime details for anime title: {}", title);
        redisCacheService.set(cacheKey, anime);
        return Optional.of(anime);
    }

    public Optional<UserProfile> getUserProfileDetails(String username) {
        var cacheKey = "mal|user:%s".formatted(username);
        var cached = redisCacheService.get(cacheKey, UserProfile.class);
        if (cached.isPresent()) {
            log.debug("Cache hit for MAL username: {}", username);
            return cached;
        }

        var response = jikanClient.getUserProfile(username);
        if (response == null || response.data() == null) {
            return Optional.empty();
        }

        log.debug("Saving user profile for MAL username: {}", username);
        redisCacheService.set(cacheKey, response.data());
        return Optional.of(response.data());
    }
}
