package dev.quasemago.aura.client.infra.searches.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MangaSearchResponseDTO {
    private Pagination pagination;
    private List<Manga> data;

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
    public static class Manga {
        private Integer mal_id;
        @JsonSetter(nulls = Nulls.SKIP)
        private String url = "N/A";
        private Images images;
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
        private Integer chapters;
        private Integer volumes;
        @JsonSetter(nulls = Nulls.SKIP)
        private String status = "N/A";
        private Boolean publishing;
        private Published published;
        private Double score;
        private Double scored;
        private Integer scored_by;
        private Integer rank;
        private Integer popularity;
        private Integer members;
        private Integer favorites;
        @JsonSetter(nulls = Nulls.SKIP)
        private String synopsis = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String background = "N/A";
        private List<Author> authors;
        private List<Serialization> serializations;
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
    public static class Title {
        @JsonSetter(nulls = Nulls.SKIP)
        private String type = "N/A";
        @JsonSetter(nulls = Nulls.SKIP)
        private String title = "N/A";
    }

    @Getter
    @Setter
    public static class Published {
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
    public static class Author {
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
    public static class Serialization {
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
