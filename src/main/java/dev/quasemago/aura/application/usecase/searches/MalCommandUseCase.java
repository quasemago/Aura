package dev.quasemago.aura.application.usecase.searches;

import dev.quasemago.aura.infrastructure.i18n.TranslationService;
import dev.quasemago.aura.infrastructure.jikan.JikanService;
import dev.quasemago.aura.infrastructure.jikan.dto.AnimeStats;
import dev.quasemago.aura.infrastructure.jikan.dto.UserProfile;
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
public class MalCommandUseCase {
    private final JikanService jikanService;
    private final TranslationService translate;

    public MalCommandUseCase(JikanService jikanService, TranslationService translate) {
        this.jikanService = jikanService;
        this.translate = translate;
    }

    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        var nameOption = translate.t("CMD_MAL_OPTION_NAME_NAME");
        return DiscordHelpers.getRequiredOption(event, nameOption)
                .map(option -> option.asString())
                .flatMap(username -> event.deferReply()
                        .then(Mono.fromCallable(() -> jikanService.getUserProfileDetails(username))
                                .subscribeOn(Schedulers.boundedElastic()))
                        .flatMap(profile -> profile
                                .map(value -> event.editReply()
                                        .withEmbeds(buildUserProfileEmbed(author, value))
                                        .then())
                                .orElseGet(() -> event.editReply(translate.t("CMD_MAL_NOT_FOUND", Map.of("name", username))).then())));
    }

    private EmbedCreateSpec buildUserProfileEmbed(User author, UserProfile userProfile) {
        var defaultValue = translate.t("COMMON_NOT_AVAILABLE");
        var animeStats = userProfile.statistics() != null ? userProfile.statistics().anime() : null;
        var imageUrl = userProfile.images() != null && userProfile.images().jpg() != null
                ? userProfile.images().jpg().imageUrl()
                : null;

        var embed = EmbedCreateSpec.builder()
                .title(translate.t("CMD_MAL_PROFILE_TITLE", Map.of(
                        "username",
                        DiscordHelpers.valueOrDefault(userProfile.username(), defaultValue)
                )))
                .url(DiscordHelpers.valueOrDefault(userProfile.url(), "#"))
                .color(Color.of(0x00ff00))
                .timestamp(Instant.now())
                .footer(translate.t("COMMON_REQUESTED_BY", Map.of("username", author.getUsername())), author.getAvatarUrl())
                .addField(translate.t("CMD_MAL_FIELD_CURRENTLY_WATCHING"), stat(animeStats, AnimeStats::watching, defaultValue), true)
                .addField(translate.t("CMD_MAL_FIELD_COMPLETED"), stat(animeStats, AnimeStats::completed, defaultValue), true)
                .addField(translate.t("CMD_MAL_FIELD_ON_HOLD"), stat(animeStats, AnimeStats::onHold, defaultValue), true)
                .addField(translate.t("CMD_MAL_FIELD_DROPPED"), stat(animeStats, AnimeStats::dropped, defaultValue), true)
                .addField(translate.t("CMD_MAL_FIELD_PLAN_TO_WATCH"), stat(animeStats, AnimeStats::planToWatch, defaultValue), true)
                .addField(translate.t("CMD_MAL_FIELD_TOTAL_DAYS"), "%s | %s".formatted(
                        stat(animeStats, AnimeStats::totalEntries, defaultValue),
                        stat(animeStats, AnimeStats::daysWatched, defaultValue)
                ), true)
                .addField(translate.t("CMD_MAL_FIELD_MEAN_SCORE"), stat(animeStats, AnimeStats::meanScore, defaultValue), true)
                .addField(translate.t("CMD_MAL_FIELD_LAST_ONLINE"), DiscordHelpers.valueOrDefault(userProfile.lastOnline(), defaultValue), true);

        if (imageUrl != null && !imageUrl.isBlank()) {
            embed.thumbnail(imageUrl);
        }

        return embed.build();
    }

    private <T> String stat(AnimeStats stats, StatGetter<T> getter, String defaultValue) {
        if (stats == null) {
            return defaultValue;
        }
        return DiscordHelpers.valueOrDefault(getter.get(stats), defaultValue);
    }

    @FunctionalInterface
    private interface StatGetter<T> {
        T get(AnimeStats stats);
    }
}
