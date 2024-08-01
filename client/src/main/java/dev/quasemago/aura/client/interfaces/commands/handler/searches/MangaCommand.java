package dev.quasemago.aura.client.interfaces.commands.handler.searches;

import dev.quasemago.aura.client.infra.searches.jikan.JikanSearchClient;
import dev.quasemago.aura.client.infra.searches.jikan.dto.MangaSearchResponseDTO;
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
public class MangaCommand extends AbstractSlashCommand {
    private final JikanSearchClient jikanSearchClient;

    public MangaCommand(JikanSearchClient jikanSearchClient) {
        this.jikanSearchClient = jikanSearchClient;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        return getDiscordCommandArgValue(event, "title")
                .map(ApplicationCommandInteractionOptionValue::asString)
                .flatMap(title -> fetchMangaData(event, title)
                        .flatMap(mangaResponse -> handleMangaResponse(event, author, mangaResponse, title))
                        .onErrorResume(error -> {
                            Logger.log.error("Error while executing manga command: {}", error.getMessage());
                            return event.editReply("Failed to retrieve manga data.").then();
                        }))
                .then();
    }

    private Mono<MangaSearchResponseDTO> fetchMangaData(ChatInputInteractionEvent event, String title) {
        return event.deferReply()
                .then(Mono.fromCallable(() -> jikanSearchClient.getMangaByTitle(title, 1, true)));
    }

    private Mono<Void> handleMangaResponse(ChatInputInteractionEvent event, User author, MangaSearchResponseDTO mangaResponse, String title) {
        if (mangaResponse == null || mangaResponse.getData() == null || mangaResponse.getData().isEmpty()) {
            return event.editReply("No manga was found with the title: `" + title + "`").then();
        }

        final var mangaData = mangaResponse.getData().get(0);
        final var embed = buildEmbed(author, mangaData);

        return event.editReply()
                .withEmbeds(embed)
                .then();
    }

    private EmbedCreateSpec buildEmbed(User author, MangaSearchResponseDTO.Manga mangaData) {
        final String synopsis = Optional.ofNullable(mangaData.getSynopsis())
                .map(s -> s.length() > 450 ? s.substring(0, 450) + "..." : s)
                .orElse("N/A");
        final String genres = joinStringList(mangaData.getGenres().stream()
                        .map(MangaSearchResponseDTO.Genre::getName)
                        .toList(),
                ", ",
                "N/A");
        final String authors = joinStringList(mangaData.getAuthors().stream()
                        .map(MangaSearchResponseDTO.Author::getName)
                        .toList(),
                ", ",
                "N/A");

        return EmbedCreateSpec.builder()
                .title(mangaData.getTitle())
                .url(mangaData.getUrl())
                .image(mangaData.getImages().getJpg().getImage_url())
                .color(Color.of(0x00FF00))
                .timestamp(Instant.now())
                .description("**English Title:** " + mangaData.getTitle_english()
                        + "\n\n**Synopsis:** " + synopsis)
                .addField("Chapters", convertObjectToString(mangaData.getChapters(), "N/A"), true)
                .addField("Type | Status", mangaData.getType() + " | " + mangaData.getStatus(), true)
                .addField("Score | Rank", convertObjectToString(mangaData.getScore(), "N/A") + " | #"
                        + mangaData.getRank(), true)
                .addField("Genre(s)", genres, true)
                .addField("Published", convertObjectToString(mangaData.getPublished().getProp().getFrom().getYear(),
                        "N/A"), true)
                .addField("Author(s)", authors, true)
                .footer("Requested by " + author.getUsername(), author.getAvatarUrl())
                .build();
    }

    @Override
    public String getName() {
        return "manga";
    }

    @Override
    public String getDescription() {
        return "Search for an manga.";
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
                        .description("The title of the manga.")
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
