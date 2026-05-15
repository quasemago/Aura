package dev.quasemago.aura.infrastructure.jikan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MalSearchResponse(UserProfile data) {
}
