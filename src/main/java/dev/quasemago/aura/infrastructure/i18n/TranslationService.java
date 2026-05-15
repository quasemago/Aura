package dev.quasemago.aura.infrastructure.i18n;

import dev.quasemago.aura.config.AuraProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TranslationService {
    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);
    private static final TypeReference<Map<String, String>> TRANSLATION_MAP = new TypeReference<>() {
    };

    private final AuraProperties properties;
    private final ObjectMapper objectMapper;
    private final Map<String, Map<String, String>> cache = new ConcurrentHashMap<>();

    public TranslationService(AuraProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public String t(String key) {
        return t(key, Map.of());
    }

    public String t(String key, Map<String, ?> values) {
        var language = properties.getBot().getDefaultLanguage();
        var translations = cache.computeIfAbsent(language, this::readLanguage);
        var translated = translations.getOrDefault(key, key);

        for (var entry : values.entrySet()) {
            translated = translated.replace("{{" + entry.getKey() + "}}", String.valueOf(entry.getValue()));
        }

        return translated;
    }

    private Map<String, String> readLanguage(String language) {
        var resource = new ClassPathResource("locales/%s.json".formatted(language));
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, TRANSLATION_MAP);
        } catch (IOException error) {
            log.error("Failed to read locale {}", language, error);
            return Map.of();
        }
    }
}
