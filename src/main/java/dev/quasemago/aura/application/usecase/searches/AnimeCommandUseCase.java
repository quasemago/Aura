package dev.quasemago.aura.application.usecase.searches;

import dev.quasemago.aura.infrastructure.i18n.TranslationService;
import dev.quasemago.aura.infrastructure.jikan.JikanService;
import dev.quasemago.aura.infrastructure.jikan.dto.Anime;
import dev.quasemago.aura.shared.DiscordHelpers;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.Map;

@Service
public class AnimeCommandUseCase {
    private final JikanService jikanService;
    private final TranslationService translate;

    public AnimeCommandUseCase(JikanService jikanService, TranslationService translate) {
        this.jikanService = jikanService;
        this.translate = translate;
    }

    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        var titleOption = translate.t("CMD_ANIME_OPTION_TITLE_NAME");
        return DiscordHelpers.getRequiredOption(event, titleOption)
                .map(option -> option.asString())
                .flatMap(title -> event.deferReply()
                        .then(Mono.fromCallable(() -> jikanService.getAnimeDetails(title, true))
                                .subscribeOn(Schedulers.boundedElastic()))
                        .flatMap(anime -> anime
                                .map(value -> event.editReply()
                                        .withEmbeds(buildAnimeEmbed(author, value))
                                        .then())
                                .orElseGet(() -> event.editReply(translate.t("CMD_ANIME_NOT_FOUND", Map.of("title", title))).then())));
    }

    private EmbedCreateSpec buildAnimeEmbed(User author, Anime anime) {
        var defaultValue = translate.t("COMMON_NOT_AVAILABLE");
        var synopsis = DiscordHelpers.truncate(anime.synopsis(), 450, defaultValue);
        var genres = DiscordHelpers.joinNames(anime.genres(), defaultValue);
        var studios = DiscordHelpers.joinNames(anime.studios(), defaultValue);
        var imageUrl = anime.images() != null && anime.images().jpg() != null
                ? anime.images().jpg().imageUrl()
                : null;
        var trailerUrl = anime.trailer() != null ? anime.trailer().url() : null;
        var airedYear = anime.aired() != null && anime.aired().prop() != null && anime.aired().prop().from() != null
                ? anime.aired().prop().from().year()
                : null;

        var embed = EmbedCreateSpec.builder()
                .title(DiscordHelpers.valueOrDefault(anime.title(), defaultValue))
                .url(DiscordHelpers.valueOrDefault(anime.url(), "#"))
                .description("""
                        **%s:** %s

                        **%s:** %s

                        **%s:** %s""".formatted(
                        translate.t("CMD_ANIME_DESCRIPTION_ENGLISH_TITLE"),
                        DiscordHelpers.valueOrDefault(anime.titleEnglish(), defaultValue),
                        translate.t("CMD_ANIME_DESCRIPTION_SYNOPSIS"),
                        synopsis,
                        translate.t("CMD_ANIME_DESCRIPTION_TRAILER"),
                        DiscordHelpers.valueOrDefault(trailerUrl, defaultValue)
                ))
                .color(Color.of(0x00ff00))
                .timestamp(Instant.now())
                .footer(translate.t("COMMON_REQUESTED_BY", Map.of("username", author.getUsername())), author.getAvatarUrl())
                .addField(translate.t("CMD_ANIME_FIELD_EPISODES"), DiscordHelpers.valueOrDefault(anime.episodes(), defaultValue), true)
                .addField(translate.t("CMD_ANIME_FIELD_TYPE_STATUS"), "%s | %s".formatted(
                        DiscordHelpers.valueOrDefault(anime.type(), defaultValue),
                        DiscordHelpers.valueOrDefault(anime.status(), defaultValue)
                ), true)
                .addField(translate.t("CMD_ANIME_FIELD_SCORE_RANK"), "%s | #%s".formatted(
                        DiscordHelpers.valueOrDefault(anime.score(), defaultValue),
                        DiscordHelpers.valueOrDefault(anime.rank(), defaultValue)
                ), true)
                .addField(translate.t("CMD_ANIME_FIELD_GENRES"), genres, true)
                .addField(translate.t("CMD_ANIME_FIELD_AIRED"), "%s | %s".formatted(
                        DiscordHelpers.valueOrDefault(airedYear, defaultValue),
                        DiscordHelpers.valueOrDefault(anime.season(), defaultValue)
                ), true)
                .addField(translate.t("CMD_ANIME_FIELD_STUDIOS"), studios, true);

        if (imageUrl != null && !imageUrl.isBlank()) {
            embed.image(imageUrl);
        }

        return embed.build();
    }
}
