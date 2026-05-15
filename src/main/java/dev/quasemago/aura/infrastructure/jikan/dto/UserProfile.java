package dev.quasemago.aura.infrastructure.jikan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserProfile(
        @JsonProperty("mal_id") Integer malId,
        String username,
        String url,
        ImageSet images,
        @JsonProperty("last_online") String lastOnline,
        Statistics statistics
) {
}
