package dev.quasemago.aura.client.interfaces.commands.handler.searches;

import dev.quasemago.aura.client.infra.searches.jikan.JikanSearchClient;
import dev.quasemago.aura.client.infra.searches.jikan.dto.MalSearchResponseDTO;
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

import static dev.quasemago.aura.client.shared.util.Helpers.convertObjectToString;
import static dev.quasemago.aura.client.shared.util.Helpers.getDiscordCommandArgValue;

@Component
public class MalCommand extends AbstractSlashCommand {
    private final JikanSearchClient jikanSearchClient;

    public MalCommand(JikanSearchClient jikanSearchClient) {
        this.jikanSearchClient = jikanSearchClient;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        return getDiscordCommandArgValue(event, "name")
                .map(ApplicationCommandInteractionOptionValue::asString)
                .flatMap(name -> fetchMalData(event, name)
                        .flatMap(malResponse -> handleMalResponse(event, author, malResponse, name))
                        .onErrorResume(error -> {
                            Logger.log.error("Error while executing mal command: {}", error.getMessage());
                            return event.editReply("Failed to retrieve mal profile data.").then();
                        }))
                .then();
    }

    private Mono<MalSearchResponseDTO> fetchMalData(ChatInputInteractionEvent event, String name) {
        return event.deferReply()
                .then(Mono.fromCallable(() -> jikanSearchClient.getMalUserByName(name)));
    }

    private Mono<Void> handleMalResponse(ChatInputInteractionEvent event, User author, MalSearchResponseDTO malResponse, String name) {
        if (malResponse == null || malResponse.getData() == null) {
            return event.editReply("No user was found with name: `" + name + "`").then();
        }

        final var malData = malResponse.getData();
        final var embed = buildEmbed(author, malData);

        return event.editReply()
                .withEmbeds(embed)
                .then();
    }

    private EmbedCreateSpec buildEmbed(User author, MalSearchResponseDTO.UserProfile malData) {
        final var animeStats = malData.getStatistics().getAnime();
        final String lastSession = convertObjectToString(malData.getLast_online(), "N/A");
        return EmbedCreateSpec.builder()
                .title("MyAnimeList Profile: " + malData.getUsername())
                .color(Color.of(0x00FF00))
                .url(malData.getUrl())
                .thumbnail(malData.getImages().getJpg().getImage_url())
                .timestamp(Instant.now())
                .addField(":green_heart: Currently Watching", convertObjectToString(animeStats.getWatching(),
                        "N/A"), true)
                .addField(":blue_heart: Completed", convertObjectToString(animeStats.getCompleted(),
                        "N/A"), true)
                .addField(":yellow_heart: On Hold", convertObjectToString(animeStats.getOn_hold(),
                        "N/A"), true)
                .addField(":broken_heart: Dropped", convertObjectToString(animeStats.getDropped(),
                        "N/A"), true)
                .addField(":white_circle: Plan to Watch", convertObjectToString(animeStats.getPlan_to_watch(),
                        "N/A"), true)
                .addField(":page_facing_up: Total | Days", convertObjectToString(animeStats.getTotal_entries(),
                        "N/A") + " | " +
                        animeStats.getDays_watched(), true)
                .addField(":bar_chart: Mean Score", convertObjectToString(animeStats.getMean_score(),
                        "N/A"), true)
                .addField(":date: Last Online", lastSession, true)
                .footer("Requested by " + author.getUsername(), author.getAvatarUrl())
                .build();
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
