package dev.quasemago.aura.infrastructure.jikan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Anime(
        @JsonProperty("mal_id") Integer malId,
        String url,
        ImageSet images,
        Trailer trailer,
        String title,
        @JsonProperty("title_english") String titleEnglish,
        String type,
        Integer episodes,
        String status,
        Aired aired,
        Double score,
        Integer rank,
        String synopsis,
        String season,
        List<NamedResource> studios,
        List<NamedResource> genres
) {
}
