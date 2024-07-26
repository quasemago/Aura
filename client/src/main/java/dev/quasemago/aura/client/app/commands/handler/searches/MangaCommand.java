package dev.quasemago.aura.client.app.commands.handler.searches;

import dev.quasemago.aura.client.app.commands.CommandCategory;
import dev.quasemago.aura.client.app.commands.SlashCommand;
import dev.quasemago.aura.client.infra.searches.JikanSearchClient;
import dev.quasemago.aura.client.infra.searches.dto.MangaSearchResponseDTO;
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
public class MangaCommand implements SlashCommand {
    private final JikanSearchClient jikanSearchClient;

    public MangaCommand(JikanSearchClient jikanSearchClient) {
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
                .then(Mono.fromCallable(() -> jikanSearchClient.getMangaByTitle(titleStr, 1, true))
                        .flatMap(mangaResponse -> {
                            if (mangaResponse == null || mangaResponse.getData() == null || mangaResponse.getData().isEmpty()) {
                                return event.editReply("No manga was found with the title: `" + titleStr + "`").then();
                            }

                            final var mangaData = mangaResponse.getData().get(0);

                            final String genres = joinNames(mangaData.getGenres().stream().map(MangaSearchResponseDTO.Genre::getName).toList());
                            final String authors = joinNames(mangaData.getAuthors().stream().map(MangaSearchResponseDTO.Author::getName).toList());
                            final String synopsis = Optional.ofNullable(mangaData.getSynopsis()).map(s -> s.length() > 450 ? s.substring(0, 450) + "..." : s).orElse("N/A");

                            final var embed = EmbedCreateSpec.builder()
                                    .title(mangaData.getTitle())
                                    .url(mangaData.getUrl())
                                    .image(mangaData.getImages().getJpg().getImage_url())
                                    .color(Color.of(0x00FF00))
                                    .timestamp(Instant.now())
                                    .description("**English Title:** " + mangaData.getTitle_english() + "\n\n**Synopsis:** " + synopsis)
                                    .addField("Chapters", convertToString(mangaData.getChapters()), true)
                                    .addField("Type | Status", mangaData.getType() + " | " + mangaData.getStatus(), true)
                                    .addField("Score | Rank", convertToString(mangaData.getScore()) + " | #"
                                            + mangaData.getRank(), true)
                                    .addField("Genre(s)", genres, true)
                                    .addField("Published", convertToString(mangaData.getPublished().getProp().getFrom().getYear()), true)
                                    .addField("Author(s)", authors, true)
                                    .footer("Requested by " + author.getUsername(), author.getAvatarUrl())
                                    .build();

                            return event.editReply().withEmbeds(embed).then();
                        })
                        .onErrorResume(err -> {
                            Logger.log.error("Error while executing manga command: {}", err.getMessage());
                            return event.editReply("Failed to retrieve manga data.").then();
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
        return "manga";
    }

    @Override
    public String description() {
        return "Search for an manga.";
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
