package dev.quasemago.aura.client.infra.searches.dto;

import java.util.List;

public record AnimeSearchResponseDTO(
        PaginationDTO pagination,
        List<AnimeDTO> data
) {
    public record PaginationDTO(
            Integer last_visible_page,
            Boolean has_next_page,
            Integer current_page,
            ItemsDTO items
    ) {
    }

    public record ItemsDTO(
            Integer count,
            Integer total,
            Integer per_page
    ) {
    }

    public record AnimeDTO(
            Integer mal_id,
            String url,
            ImagesDTO images,
            TrailerDTO trailer,
            Boolean approved,
            List<TitleDTO> titles,
            String title,
            String title_english,
            String title_japanese,
            List<String> title_synonyms,
            String type,
            String source,
            Integer episodes,
            String status,
            Boolean airing,
            AiredDTO aired,
            String duration,
            String rating,
            Double score,
            Integer scored_by,
            Integer rank,
            Integer popularity,
            Integer members,
            Integer favorites,
            String synopsis,
            String background,
            String season,
            Integer year,
            BroadcastDTO broadcast,
            List<ProducerDTO> producers,
            List<ProducerDTO> licensors,
            List<ProducerDTO> studios,
            List<GenreDTO> genres,
            List<ThemeDTO> themes,
            List<DemographicDTO> demographics
    ) {
    }

    public record ImagesDTO(
            ImageFormatDTO jpg,
            ImageFormatDTO webp
    ) {
    }

    public record ImageFormatDTO(
            String image_url,
            String small_image_url,
            String large_image_url
    ) {
    }

    public record TrailerDTO(
            String youtube_id,
            String url,
            String embed_url,
            TrailerImagesDTO images
    ) {
    }

    public record TrailerImagesDTO(
            String image_url,
            String small_image_url,
            String medium_image_url,
            String large_image_url,
            String maximum_image_url
    ) {
    }

    public record TitleDTO(
            String type,
            String title
    ) {
    }

    public record AiredDTO(
            String from,
            String to,
            PropDTO prop,
            String string
    ) {
    }

    public record PropDTO(
            FromToDTO from,
            FromToDTO to
    ) {
    }

    public record FromToDTO(
            Integer day,
            Integer month,
            Integer year
    ) {
    }

    public record BroadcastDTO(
            String day,
            String time,
            String timezone,
            String string
    ) {
    }

    public record ProducerDTO(
            Integer mal_id,
            String type,
            String name,
            String url
    ) {
    }

    public record GenreDTO(
            Integer mal_id,
            String type,
            String name,
            String url
    ) {
    }

    public record ThemeDTO(
            Integer mal_id,
            String type,
            String name,
            String url
    ) {
    }

    public record DemographicDTO(
            Integer mal_id,
            String type,
            String name,
            String url
    ) {
    }
}
