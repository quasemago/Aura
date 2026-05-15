package dev.quasemago.aura.infrastructure.jikan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ImageFormat(
        @JsonProperty("image_url") String imageUrl,
        @JsonProperty("small_image_url") String smallImageUrl,
        @JsonProperty("large_image_url") String largeImageUrl
) {
}
