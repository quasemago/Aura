package dev.quasemago.aura.client.infra.searches.dto;

import java.util.List;

public record MalSearchResponseDTO(
        UserProfileDTO data
) {
    public record UserProfileDTO(
            Integer mal_id,
            String username,
            String url,
            ImagesDTO images,
            String last_online,
            String gender,
            String birthday,
            String location,
            String joined,
            StatisticsDTO statistics,
            FavoritesDTO favorites,
            UpdatesDTO updates,
            String about,
            List<ExternalLinkDTO> external
    ) {
    }

    public record ImagesDTO(
            ImageFormatDTO jpg,
            ImageFormatDTO webp
    ) {
    }

    public record ImageFormatDTO(
            String image_url
    ) {
    }

    public record StatisticsDTO(
            AnimeStatsDTO anime,
            MangaStatsDTO manga
    ) {
    }

    public record AnimeStatsDTO(
            Double days_watched,
            Double mean_score,
            Integer watching,
            Integer completed,
            Integer on_hold,
            Integer dropped,
            Integer plan_to_watch,
            Integer total_entries,
            Integer rewatched,
            Integer episodes_watched
    ) {
    }

    public record MangaStatsDTO(
            Double days_read,
            Double mean_score,
            Integer reading,
            Integer completed,
            Integer on_hold,
            Integer dropped,
            Integer plan_to_read,
            Integer total_entries,
            Integer reread,
            Integer chapters_read,
            Integer volumes_read
    ) {
    }

    public record FavoritesDTO(
            List<FavoriteAnimeDTO> anime,
            List<FavoriteMangaDTO> manga,
            List<FavoriteCharacterDTO> characters,
            List<FavoritePeopleDTO> people
    ) {
    }

    public record FavoriteAnimeDTO(
            Integer mal_id,
            String url,
            ImagesDTO images,
            String title,
            String type,
            Integer start_year
    ) {
    }

    public record FavoriteMangaDTO(
            Integer mal_id,
            String url,
            ImagesDTO images,
            String title,
            String type,
            Integer start_year
    ) {
    }

    public record FavoriteCharacterDTO(
            Integer mal_id,
            String url,
            ImagesDTO images,
            String name
    ) {
    }

    public record FavoritePeopleDTO(
            Integer mal_id,
            String url,
            ImagesDTO images,
            String name
    ) {
    }

    public record UpdatesDTO(
            List<AnimeUpdateDTO> anime,
            List<MangaUpdateDTO> manga
    ) {
    }

    public record AnimeUpdateDTO(
            AnimeEntryDTO entry,
            Double score,
            String status,
            Integer episodes_seen,
            Integer episodes_total,
            String date
    ) {
    }

    public record MangaUpdateDTO(
            MangaEntryDTO entry,
            Double score,
            String status,
            Integer chapters_read,
            Integer chapters_total,
            String date
    ) {
    }

    public record AnimeEntryDTO(
            Integer mal_id,
            String url,
            ImagesDTO images,
            String title
    ) {
    }

    public record MangaEntryDTO(
            Integer mal_id,
            String url,
            ImagesDTO images,
            String title
    ) {
    }

    public record ExternalLinkDTO(
            String name,
            String url
    ) {
    }
}