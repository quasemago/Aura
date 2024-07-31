package dev.quasemago.aura.client.app.commands.handler.general;

import dev.quasemago.aura.client.app.commands.CommandCategory;
import dev.quasemago.aura.client.app.commands.SlashCommand;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class AboutCommand implements SlashCommand {
    private final GatewayDiscordClient gatewayClient;

    public AboutCommand(GatewayDiscordClient gatewayClient) {
        this.gatewayClient = gatewayClient;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        return gatewayClient.getSelf()
                .flatMap(bot -> {
                    final var botName = bot.getUsername();
                    final var botAvatar = bot.getAvatarUrl();

                    final var embed = EmbedCreateSpec.builder()
                            .title("\uD83E\uDD16 " + botName)
                            .description("Aura is a Discord bot that provides a variety of features to enhance your server.")
                            .color(Color.of(0x0099ff))
                            .thumbnail(botAvatar)
                            .timestamp(Instant.now())
                            .addField("Created by", "quasemago [(Github)](https://github.com/quasemago)", true)
                            .addField("Version", "0.0.1", true)
                            .footer("Requested by " + author.getUsername(), author.getAvatarUrl())
                            .build();

                    return event.reply()
                            .withEmbeds(embed)
                            .withEphemeral(true);
                });
    }

    @Override
    public String getName() {
        return "about";
    }

    @Override
    public String getDescription() {
        return "Help command with information about the bot.";
    }

    @Override
    public ApplicationCommandRequest getCommand() {
        return ApplicationCommandRequest.builder()
                .name(getName())
                .description(getDescription())
                .dmPermission(true)
                .defaultMemberPermissions(String.valueOf(getPermission().getValue()))
                .build();
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERAL;
    }
}
