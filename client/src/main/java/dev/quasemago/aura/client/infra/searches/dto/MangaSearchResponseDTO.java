package dev.quasemago.aura.client.infra.searches.dto;

import java.util.List;

public record MangaSearchResponseDTO(
        PaginationDTO pagination,
        List<MangaDTO> data
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

    public record MangaDTO(
            Integer mal_id,
            String url,
            ImagesDTO images,
            Boolean approved,
            List<TitleDTO> titles,
            String title,
            String title_english,
            String title_japanese,
            List<String> title_synonyms,
            String type,
            Integer chapters,
            Integer volumes,
            String status,
            Boolean publishing,
            PublishedDTO published,
            Double score,
            Double scored,
            Integer scored_by,
            Integer rank,
            Integer popularity,
            Integer members,
            Integer favorites,
            String synopsis,
            String background,
            List<AuthorDTO> authors,
            List<SerializationDTO> serializations,
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

    public record TitleDTO(
            String type,
            String title
    ) {
    }

    public record PublishedDTO(
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

    public record AuthorDTO(
            Integer mal_id,
            String type,
            String name,
            String url
    ) {
    }

    public record SerializationDTO(
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
