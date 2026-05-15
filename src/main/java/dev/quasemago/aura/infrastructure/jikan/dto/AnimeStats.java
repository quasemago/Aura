package dev.quasemago.aura.infrastructure.jikan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AnimeStats(
        @JsonProperty("days_watched") Double daysWatched,
        @JsonProperty("mean_score") Double meanScore,
        Integer watching,
        Integer completed,
        @JsonProperty("on_hold") Integer onHold,
        Integer dropped,
        @JsonProperty("plan_to_watch") Integer planToWatch,
        @JsonProperty("total_entries") Integer totalEntries
) {
}
