package dev.quasemago.aura.client.infra.searches;

import dev.quasemago.aura.client.infra.searches.dto.AnimeSearchResponseDTO;
import dev.quasemago.aura.client.infra.searches.dto.MangaSearchResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "anime-search-client",
        url = "${jikan.api-url}"
)
public interface JikanSearchClient {
    @GetMapping("/anime")
    AnimeSearchResponseDTO getAnimeByTitle(@RequestParam("q") String title,
                                           @RequestParam("limit") int limit,
                                           @RequestParam("sfw") boolean sfw);

    @GetMapping("/manga")
    MangaSearchResponseDTO getMangaByTitle(@RequestParam("q") String title,
                                           @RequestParam("limit") int limit,
                                           @RequestParam("sfw") boolean sfw);
}
