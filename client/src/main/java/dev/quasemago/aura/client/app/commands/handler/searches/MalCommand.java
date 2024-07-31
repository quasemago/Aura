package dev.quasemago.aura.client.app.commands.handler.searches;

import dev.quasemago.aura.client.app.commands.CommandCategory;
import dev.quasemago.aura.client.app.commands.SlashCommand;
import dev.quasemago.aura.client.infra.searches.JikanSearchClient;
import dev.quasemago.aura.client.shared.util.Logger;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
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

@Component
public class MalCommand implements SlashCommand {
    private final JikanSearchClient jikanSearchClient;

    public MalCommand(JikanSearchClient jikanSearchClient) {
        this.jikanSearchClient = jikanSearchClient;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        final var interaction = event.getInteraction()
                .getCommandInteraction()
                .orElseThrow();

        final var name = interaction.getOption("name")
                .flatMap(option -> option.getValue())
                .orElseThrow();

        final String nameStr = name.asString();
        return event.deferReply()
                .then(Mono.fromCallable(() -> jikanSearchClient.getMalUserByName(nameStr))
                        .flatMap(malResponse -> {
                            if (malResponse == null || malResponse.getData() == null) {
                                return event.editReply("No user was found with the getName: `" + nameStr + "`").then();
                            }

                            final var malData = malResponse.getData();
                            final var animeStats = malData.getStatistics().getAnime();
                            final String lastSession = convertToString(malData.getLast_online());

                            final var embed = EmbedCreateSpec.builder()
                                    .title("MyAnimeList Profile: " + malData.getUsername())
                                    .color(Color.of(0x00FF00))
                                    .url(malData.getUrl())
                                    .thumbnail(malData.getImages().getJpg().getImage_url())
                                    .timestamp(Instant.now())
                                    .addField(":green_heart: Currently Watching", convertToString(animeStats.getWatching()), true)
                                    .addField(":blue_heart: Completed", convertToString(animeStats.getCompleted()), true)
                                    .addField(":yellow_heart: On Hold", convertToString(animeStats.getOn_hold()), true)
                                    .addField(":broken_heart: Dropped", convertToString(animeStats.getDropped()), true)
                                    .addField(":white_circle: Plan to Watch", convertToString(animeStats.getPlan_to_watch()), true)
                                    .addField(":page_facing_up: Total | Days", convertToString(animeStats.getTotal_entries()) + " | " +
                                            animeStats.getDays_watched(), true)
                                    .addField(":bar_chart: Mean Score", convertToString(animeStats.getMean_score()), true)
                                    .addField(":date: Last Online", lastSession, true)
                                    .footer("Requested by " + author.getUsername(), author.getAvatarUrl())
                                    .build();

                            return event.editReply().withEmbeds(embed).then();
                        })
                        .onErrorResume(err -> {
                            Logger.log.error("Error while executing mal command: {}", err.getMessage());
                            return event.editReply("Failed to retrieve mal profile data.").then();
                        })
                        .then()
                );
    }

    private String convertToString(Object obj) {
        return Optional.ofNullable(obj).map(Object::toString).orElse("N/A");
    }

    @Override
    public String getName() {
        return "mal";
    }

    @Override
    public String getDescription() {
        return "Search for a user's MyAnimeList profile.";
    }

    @Override
    public ApplicationCommandRequest getCommand() {
        return ApplicationCommandRequest.builder()
                .name(getName())
                .description(getDescription())
                .dmPermission(false)
                .defaultMemberPermissions(String.valueOf(getPermission().getValue()))
                .addOption(ApplicationCommandOptionData.builder()
                        .name("name")
                        .description("The getName of the user.")
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
