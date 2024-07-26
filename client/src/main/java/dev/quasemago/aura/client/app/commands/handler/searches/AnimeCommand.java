package dev.quasemago.aura.client.app.commands.handler.searches;

import dev.quasemago.aura.client.app.commands.CommandCategory;
import dev.quasemago.aura.client.app.commands.SlashCommand;
import dev.quasemago.aura.client.infra.searches.JikanSearchClient;
import dev.quasemago.aura.client.infra.searches.dto.AnimeSearchResponseDTO;
import dev.quasemago.aura.client.shared.util.Logger;
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
                            if (animeResponse == null || animeResponse.getData() == null || animeResponse.getData().isEmpty()) {
                                return event.editReply("No anime was found with the title: `" + titleStr + "`").then();
                            }

                            final var animeData = animeResponse.getData().get(0);

                            final String genres = joinNames(animeData.getGenres().stream().map(AnimeSearchResponseDTO.Genre::getName).toList());
                            final String studios = joinNames(animeData.getStudios().stream().map(AnimeSearchResponseDTO.Producer::getName).toList());
                            final String synopsis = Optional.ofNullable(animeData.getSynopsis()).map(s -> s.length() > 450 ? s.substring(0, 450) + "..." : s).orElse("N/A");

                            final var embed = EmbedCreateSpec.builder()
                                    .title(animeData.getTitle())
                                    .url(animeData.getUrl())
                                    .image(animeData.getImages().getJpg().getImage_url())
                                    .color(Color.of(0x00FF00))
                                    .timestamp(Instant.now())
                                    .description("**English Title:** " + animeData.getTitle_english() + "\n\n**Synopsis:** " + synopsis
                                            + "\n\n**Trailer:** " + animeData.getTrailer().getUrl())
                                    .addField("Episodes", convertToString(animeData.getEpisodes()), true)
                                    .addField("Type | Status", animeData.getType() + " | " + animeData.getStatus(), true)
                                    .addField("Score | Rank", convertToString(animeData.getScore()) + " | #"
                                            + convertToString(animeData.getRank()), true)
                                    .addField("Genre(s)", genres, true)
                                    .addField("Aired", convertToString(animeData.getAired().getProp().getFrom().getYear())
                                            + " | " + animeData.getSeason(), true)
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

    private String convertToString(Object obj) {
        return Optional.ofNullable(obj).map(Object::toString).orElse("N/A");
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
                .dmPermission(false)
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
