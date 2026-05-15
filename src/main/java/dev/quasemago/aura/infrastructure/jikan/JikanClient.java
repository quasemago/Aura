package dev.quasemago.aura.infrastructure.jikan;

import dev.quasemago.aura.infrastructure.jikan.dto.AnimeSearchResponse;
import dev.quasemago.aura.infrastructure.jikan.dto.MalSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "jikan-client", url = "${aura.jikan.api-url}", configuration = JikanFeignConfig.class)
public interface JikanClient {

    @GetMapping("/anime")
    AnimeSearchResponse searchAnime(
            @RequestParam("q") String title,
            @RequestParam("limit") int limit,
            @RequestParam("sfw") boolean sfw
    );

    @GetMapping("/users/{username}/full")
    MalSearchResponse getUserProfile(@PathVariable("username") String username);
}
