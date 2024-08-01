package dev.quasemago.aura.client.infra.searches.jikan.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MalSearchResponseDTO {
    private UserProfile data;

    @Getter
    @Setter
    public static class UserProfile {
        private Integer mal_id;
        @JsonSetter(nulls = Nulls.SKIP)
        private String username = "N/A";
        private String url;
        private Images images;
        @JsonSetter(nulls = Nulls.SKIP)
        private String last_online = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String gender = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String birthday = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String location = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String joined = "N/A";
        private Statistics statistics;
        private Favorites favorites;
        private Updates updates;
        @JsonSetter(nulls = Nulls.SKIP)
        private String about = "N/A";
        private List<ExternalLink> external;
    }

    @Getter
    @Setter
    public static class Images {
        private ImageFormat jpg;
        private ImageFormat webp;
    }

    @Getter
    @Setter
    public static class ImageFormat {
        private String image_url;
    }

    @Getter
    @Setter
    public static class Statistics {
        private AnimeStats anime;
        private MangaStats manga;
    }

    @Getter
    @Setter
    public static class AnimeStats {
        private Double days_watched;
        private Double mean_score;
        private Integer watching;
        private Integer completed;
        private Integer on_hold;
        private Integer dropped;
        private Integer plan_to_watch;
        private Integer total_entries;
        private Integer rewatched;
        private Integer episodes_watched;
    }

    @Getter
    @Setter
    public static class MangaStats {
        private Double days_read;
        private Double mean_score;
        private Integer reading;
        private Integer completed;
        private Integer on_hold;
        private Integer dropped;
        private Integer plan_to_read;
        private Integer total_entries;
        private Integer reread;
        private Integer chapters_read;
        private Integer volumes_read;
    }

    @Getter
    @Setter
    public static class Favorites {
        private List<FavoriteAnime> anime;
        private List<FavoriteManga> manga;
        private List<FavoriteCharacter> characters;
        private List<FavoritePeople> people;
    }

    @Getter
    @Setter
    public static class FavoriteAnime {
        private Integer mal_id;
        private String url;
        private Images images;
        @JsonSetter(nulls = Nulls.SKIP)
        private String title = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String type = "N/A";
        private Integer start_year;
    }

    @Getter
    @Setter
    public static class FavoriteManga {
        private Integer mal_id;
        private String url;
        private Images images;
        @JsonSetter(nulls = Nulls.SKIP)
        private String title = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String type = "N/A";
        private Integer start_year;
    }

    @Getter
    @Setter
    public static class FavoriteCharacter {
        private Integer mal_id;
        private String url;
        private Images images;
        @JsonSetter(nulls = Nulls.SKIP)
        private String name = "N/A";
    }

    @Getter
    @Setter
    public static class FavoritePeople {
        private Integer mal_id;
        private String url;
        private Images images;
        @JsonSetter(nulls = Nulls.SKIP)
        private String name = "N/A";
    }

    @Getter
    @Setter
    public static class Updates {
        private List<AnimeUpdate> anime;
        private List<MangaUpdate> manga;
    }

    @Getter
    @Setter
    public static class AnimeUpdate {
        private AnimeEntry entry;
        private Double score;
        @JsonSetter(nulls = Nulls.SKIP)
        private String status = "N/A";
        private Integer episodes_seen;
        private Integer episodes_total;
        @JsonSetter(nulls = Nulls.SKIP)
        private String date = "N/A";
    }

    @Getter
    @Setter
    public static class MangaUpdate {
        private MangaEntry entry;
        private Double score;
        @JsonSetter(nulls = Nulls.SKIP)
        private String status = "N/A";
        private Integer chapters_read;
        private Integer chapters_total;
        @JsonSetter(nulls = Nulls.SKIP)
        private String date = "N/A";
    }

    @Getter
    @Setter
    public static class AnimeEntry {
        private Integer mal_id;
        private String url;
        private Images images;
        @JsonSetter(nulls = Nulls.SKIP)
        private String title = "N/A";
    }

    @Getter
    @Setter
    public static class MangaEntry {
        private Integer mal_id;
        private String url;
        private Images images;
        @JsonSetter(nulls = Nulls.SKIP)
        private String title = "N/A";
    }

    @Getter
    @Setter
    public static class ExternalLink {
        @JsonSetter(nulls = Nulls.SKIP)
        private String name = "N/A";
        private String url;
    }
}
