package dev.quasemago.aura.infrastructure.jikan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AnimeSearchResponse(List<Anime> data) {
}
