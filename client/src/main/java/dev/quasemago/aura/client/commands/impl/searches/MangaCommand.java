package dev.quasemago.aura.client.commands.impl.searches;

import dev.quasemago.aura.client.commands.CommandCategory;
import dev.quasemago.aura.client.commands.SlashCommand;
import dev.quasemago.aura.client.infra.searches.JikanSearchClient;
import dev.quasemago.aura.client.infra.searches.dto.MangaSearchResponseDTO;
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
                            if (mangaResponse == null || mangaResponse.data() == null || mangaResponse.data().isEmpty()) {
                                return event.editReply("No manga was found with the title: `" + titleStr + "`").then();
                            }

                            final var mangaData = mangaResponse.data().get(0);

                            String genres = joinNames(mangaData.genres().stream().map(MangaSearchResponseDTO.GenreDTO::name).toList());
                            String authors = joinNames(mangaData.authors().stream().map(MangaSearchResponseDTO.AuthorDTO::name).toList());

                            final var englishTitle = Optional.ofNullable(mangaData.title_english()).orElse("N/A");
                            final var synopsis = Optional.ofNullable(mangaData.synopsis()).map(s -> s.length() > 450 ? s.substring(0, 450) + "..." : s).orElse("N/A");
                            final var chapters = Optional.ofNullable(mangaData.chapters()).map(Object::toString).orElse("N/A");
                            final var score = Optional.ofNullable(mangaData.score()).map(Object::toString).orElse("N/A");
                            final var rank = Optional.ofNullable(mangaData.rank()).map(Object::toString).orElse("N/A");
                            final var launchData = Optional.ofNullable(mangaData.published().prop().from().year()).map(Object::toString).orElse("N/A");

                            final var embed = EmbedCreateSpec.builder()
                                    .title(mangaData.title())
                                    .url(mangaData.url())
                                    .image(mangaData.images().jpg().image_url())
                                    .color(Color.of(0x00FF00))
                                    .timestamp(Instant.now())
                                    .description("**English Title:** " + englishTitle + "\n\n**Synopsis:** " + synopsis)
                                    .addField("Chapters", chapters, true)
                                    .addField("Type | Status", mangaData.type() + " | " + mangaData.status(), true)
                                    .addField("Score | Rank", score + " | #" + rank, true)
                                    .addField("Genre(s)", genres, true)
                                    .addField("Aired From", launchData, true)
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
                .dmPermission(true)
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
