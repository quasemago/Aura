package dev.quasemago.aura.client.interfaces.commands.handler.searches;

import dev.quasemago.aura.client.infra.searches.jikan.JikanSearchClient;
import dev.quasemago.aura.client.infra.searches.jikan.dto.AnimeSearchResponseDTO;
import dev.quasemago.aura.client.interfaces.commands.AbstractSlashCommand;
import dev.quasemago.aura.client.interfaces.commands.CommandCategory;
import dev.quasemago.aura.client.shared.util.Logger;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;

import static dev.quasemago.aura.client.shared.util.Helpers.*;

@Component
public class AnimeCommand extends AbstractSlashCommand {
    private final JikanSearchClient jikanSearchClient;

    public AnimeCommand(JikanSearchClient jikanSearchClient) {
        this.jikanSearchClient = jikanSearchClient;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        return getDiscordCommandArgValue(event, "title")
                .map(ApplicationCommandInteractionOptionValue::asString)
                .flatMap(title -> fetchAnimeData(event, title)
                        .flatMap(animeResponse -> handleAnimeResponse(event, author, animeResponse, title))
                        .onErrorResume(error -> {
                            Logger.log.error("Error while executing anime command: {}", error.getMessage());
                            return event.editReply("Failed to retrieve anime data.").then();
                        }))
                .then();
    }

    private Mono<AnimeSearchResponseDTO> fetchAnimeData(ChatInputInteractionEvent event, String title) {
        return event.deferReply()
                .then(Mono.fromCallable(() -> jikanSearchClient.getAnimeByTitle(title, 1, true)));
    }

    private Mono<Void> handleAnimeResponse(ChatInputInteractionEvent event, User author, AnimeSearchResponseDTO animeResponse, String title) {
        if (animeResponse == null || animeResponse.getData() == null || animeResponse.getData().isEmpty()) {
            return event.editReply("No anime was found with the title: `" + title + "`").then();
        }

        final var animeData = animeResponse.getData().get(0);
        final var embed = buildEmbed(author, animeData);

        return event.editReply()
                .withEmbeds(embed)
                .then();
    }

    private EmbedCreateSpec buildEmbed(User author, AnimeSearchResponseDTO.Anime animeData) {
        final String synopsis = Optional.ofNullable(animeData.getSynopsis())
                .map(s -> s.length() > 450 ? s.substring(0, 450) + "..." : s)
                .orElse("N/A");
        final String genres = joinStringList(animeData.getGenres().stream()
                        .map(AnimeSearchResponseDTO.Genre::getName).toList(),
                ", ", "N/A");
        final String studios = joinStringList(animeData.getStudios().stream()
                        .map(AnimeSearchResponseDTO.Producer::getName).toList(),
                ", ", "N/A");

        return EmbedCreateSpec.builder()
                .title(animeData.getTitle())
                .url(animeData.getUrl())
                .image(animeData.getImages().getJpg().getImage_url())
                .color(Color.of(0x00FF00))
                .timestamp(Instant.now())
                .description("**English Title:** " + animeData.getTitle_english()
                        + "\n\n**Synopsis:** " + synopsis
                        + "\n\n**Trailer:** " + animeData.getTrailer().getUrl())
                .addField("Episodes", convertObjectToString(animeData.getEpisodes(), "N/A"), true)
                .addField("Type | Status", animeData.getType() + " | " + animeData.getStatus(), true)
                .addField("Score | Rank", convertObjectToString(animeData.getScore(), "N/A") + " | #"
                        + convertObjectToString(animeData.getRank(), "N/A"), true)
                .addField("Genre(s)", genres, true)
                .addField("Aired", convertObjectToString(animeData.getAired().getProp().getFrom().getYear(),
                        "N/A") + " | " + animeData.getSeason(), true)
                .addField("Studio(s)", studios, true)
                .footer("Requested by " + author.getUsername(), author.getAvatarUrl())
                .build();
    }

    @Override
    public String getName() {
        return "anime";
    }

    @Override
    public String getDescription() {
        return "Search for an anime.";
    }

    @Override
    public ApplicationCommandRequest getCommand() {
        return ApplicationCommandRequest.builder()
                .name(getName())
                .description(getDescription())
                .dmPermission(false)
                .defaultMemberPermissions(String.valueOf(getPermission().getValue()))
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
