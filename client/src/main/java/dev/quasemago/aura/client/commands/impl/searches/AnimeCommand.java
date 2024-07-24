package dev.quasemago.aura.client.commands.impl.searches;

import dev.quasemago.aura.client.commands.CommandCategory;
import dev.quasemago.aura.client.commands.SlashCommand;
import dev.quasemago.aura.client.infra.searches.JikanSearchClient;
import dev.quasemago.aura.client.infra.searches.dto.AnimeSearchResponseDTO;
import dev.quasemago.aura.client.util.Logger;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import discord4j.rest.util.Permission;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class AnimeCommand implements SlashCommand {
    private final JikanSearchClient jikanSearchClient;

    public AnimeCommand(JikanSearchClient jikanSearchClient) {
        this.jikanSearchClient = jikanSearchClient;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        final var interaction = event.getInteraction()
                .getCommandInteraction()
                .orElseThrow();

        final var title = interaction.getOption("title")
                .flatMap(option -> option.getValue())
                .orElseThrow();

        final String titleStr = title.asString();
        return event.deferReply()
                .then(Mono.fromCallable(() -> jikanSearchClient.getAnimeByTitle(titleStr, 1, true))
                        .flatMap(animeResponse -> {
                            if (animeResponse == null || animeResponse.data() == null || animeResponse.data().isEmpty()) {
                                return event.editReply("No anime was found with the title: `" + titleStr + "`").then();
                            }

                            final var animeData = animeResponse.data().get(0);

                            String genres = joinNames(animeData.genres().stream().map(AnimeSearchResponseDTO.GenreDTO::name).toList());
                            String studios = joinNames(animeData.studios().stream().map(AnimeSearchResponseDTO.ProducerDTO::name).toList());

                            final var englishTitle = Optional.ofNullable(animeData.title_english()).orElse("N/A");
                            final var synopsis = Optional.ofNullable(animeData.synopsis()).map(s -> s.length() > 450 ? s.substring(0, 450) + "..." : s).orElse("N/A");
                            final var trailer = Optional.ofNullable(animeData.trailer().url()).orElse("N/A");
                            final var episodes = Optional.ofNullable(animeData.episodes()).map(Object::toString).orElse("N/A");
                            final var score = Optional.ofNullable(animeData.score()).map(Object::toString).orElse("N/A");
                            final var rank = Optional.ofNullable(animeData.rank()).map(Object::toString).orElse("N/A");
                            final var airedFrom = Optional.ofNullable(animeData.aired().prop().from().year()).map(Object::toString).orElse("N/A");
                            final var season = Optional.ofNullable(animeData.season()).orElse("N/A");

                            final var embed = EmbedCreateSpec.builder()
                                    .title(animeData.title())
                                    .url(animeData.url())
                                    .image(animeData.images().jpg().image_url())
                                    .color(Color.of(0x00FF00))
                                    .timestamp(Instant.now())
                                    .description("**English Title:** " + englishTitle + "\n\n**Synopsis:** " + synopsis + "\n\n**Trailer:** " + trailer)
                                    .addField("Episodes", episodes, true)
                                    .addField("Type | Status", animeData.type() + " | " + animeData.status(), true)
                                    .addField("Score | Rank", score + " | #" + rank, true)
                                    .addField("Genre(s)", genres, true)
                                    .addField("Aired From", airedFrom + " | " + season, true)
                                    .addField("Studio(s)", studios, true)
                                    .footer("Requested by " + author.getUsername(), author.getAvatarUrl())
                                    .build();

                            return event.editReply().withEmbeds(embed).then();
                        })
                        .onErrorResume(err -> {
                            Logger.log.error("Error while executing anime command: {}", err.getMessage());
                            return event.editReply("Failed to retrieve anime data.").then();
                        })
                        .then()
                );
    }

    private String joinNames(List<String> names) {
        return names == null || names.isEmpty() ? "N/A" : String.join(", ", names);
    }

    @Override
    public String name() {
        return "anime";
    }

    @Override
    public String description() {
        return "Search for an anime.";
    }

    @Override
    public Permission permission() {
        return Permission.SEND_MESSAGES;
    }

    @Override
    public ApplicationCommandRequest getCommand() {
        return ApplicationCommandRequest.builder()
                .name(name())
                .description(description())
                .dmPermission(true)
                .defaultMemberPermissions(String.valueOf(permission().getValue()))
                .addOption(ApplicationCommandOptionData.builder()
                        .name("title")
                        .description("The title of the anime.")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .build();
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.SEARCHES;
    }
}
