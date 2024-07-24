package dev.quasemago.aura.client.infra.searches;

import dev.quasemago.aura.client.infra.searches.dto.AnimeSearchResponseDTO;
import dev.quasemago.aura.client.infra.searches.dto.MalSearchResponseDTO;
import dev.quasemago.aura.client.infra.searches.dto.MangaSearchResponseDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "anime-search-client",
        url = "${jikan.api-url}"
)
public interface JikanSearchClient {
    @GetMapping("/anime")
    @Cacheable(value = "anime", key = "#title")
    AnimeSearchResponseDTO getAnimeByTitle(@RequestParam("q") String title,
                                           @RequestParam("limit") int limit,
                                           @RequestParam("sfw") boolean sfw);

    @GetMapping("/manga")
    @Cacheable(value = "manga", key = "#title")
    MangaSearchResponseDTO getMangaByTitle(@RequestParam("q") String title,
                                           @RequestParam("limit") int limit,
                                           @RequestParam("sfw") boolean sfw);

    @GetMapping("/users/{username}/full")
    @Cacheable(value = "mal-user", key = "#username")
    MalSearchResponseDTO getMalUserByName(@PathVariable("username") String username);
}
