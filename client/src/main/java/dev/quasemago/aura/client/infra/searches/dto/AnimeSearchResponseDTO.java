package dev.quasemago.aura.client.infra.searches.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimeSearchResponseDTO {
    private Pagination pagination;
    private List<Anime> data;

    @Getter
    @Setter
    public static class Pagination {
        private Integer last_visible_page;
        private Boolean has_next_page;
        private Integer current_page;
        private Items items;
    }

    @Getter
    @Setter
    public static class Items {
        private Integer count;
        private Integer total;
        private Integer per_page;
    }

    @Getter
    @Setter
    public static class Anime {
        private Integer mal_id;
        @JsonSetter(nulls = Nulls.SKIP)
        private String url = "N/A";
        private Images images;
        private Trailer trailer;
        private Boolean approved;
        private List<Title> titles;
        @JsonSetter(nulls = Nulls.SKIP)
        private String title = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String title_english = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String title_japanese = "N/A";
        private List<String> title_synonyms;
        @JsonSetter(nulls = Nulls.SKIP)
        private String type = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String source = "N/A";
        private Integer episodes;
        @JsonSetter(nulls = Nulls.SKIP)
        private String status = "N/A";
        private Boolean airing;
        private Aired aired;
        @JsonSetter(nulls = Nulls.SKIP)
        private String duration = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String rating = "N/A";
        private Double score;
        private Integer scored_by;
        private Integer rank;
        private Integer popularity;
        private Integer members;
        private Integer favorites;
        @JsonSetter(nulls = Nulls.SKIP)
        private String synopsis = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String background = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String season = "N/A";
        private Integer year;
        private Broadcast broadcast;
        private List<Producer> producers;
        private List<Producer> licensors;
        private List<Producer> studios;
        private List<Genre> genres;
        private List<Theme> themes;
        private List<Demographic> demographics;
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
        @JsonSetter(nulls = Nulls.SKIP)
        private String image_url = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String small_image_url = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String large_image_url = "N/A";
    }

    @Getter
    @Setter
    public static class Trailer {
        @JsonSetter(nulls = Nulls.SKIP)
        private String youtube_id = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String url = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String embed_url = "N/A";
        private TrailerImages images;
    }

    @Getter
    @Setter
    public static class TrailerImages {
        @JsonSetter(nulls = Nulls.SKIP)
        private String image_url = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String small_image_url = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String medium_image_url = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String large_image_url = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String maximum_image_url = "N/A";
    }

    @Getter
    @Setter
    public static class Title {
        @JsonSetter(nulls = Nulls.SKIP)
        private String type = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String title = "N/A";
    }

    @Getter
    @Setter
    public static class Aired {
        @JsonSetter(nulls = Nulls.SKIP)
        private String from = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String to = "N/A";
        private Prop prop;
        @JsonSetter(nulls = Nulls.SKIP)
        private String string = "N/A";
    }

    @Getter
    @Setter
    public static class Prop {
        private FromTo from;
        private FromTo to;
    }

    @Getter
    @Setter
    public static class FromTo {
        private Integer day;
        private Integer month;
        private Integer year;
    }

    @Getter
    @Setter
    public static class Broadcast {
        @JsonSetter(nulls = Nulls.SKIP)
        private String day = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String time = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String timezone = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String string = "N/A";
    }

    @Getter
    @Setter
    public static class Producer {
        @JsonSetter(nulls = Nulls.SKIP)
        private Integer mal_id = 0;
        @JsonSetter(nulls = Nulls.SKIP)
        private String type = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String name = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String url = "N/A";
    }

    @Getter
    @Setter
    public static class Genre {
        private Integer mal_id;
        @JsonSetter(nulls = Nulls.SKIP)
        private String type = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String name = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String url = "N/A";
    }

    @Getter
    @Setter
    public static class Theme {
        private Integer mal_id;
        @JsonSetter(nulls = Nulls.SKIP)
        private String type = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String name = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String url = "N/A";
    }

    @Getter
    @Setter
    public static class Demographic {
        private Integer mal_id;
        @JsonSetter(nulls = Nulls.SKIP)
        private String type = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String name = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String url = "N/A";
    }
}

