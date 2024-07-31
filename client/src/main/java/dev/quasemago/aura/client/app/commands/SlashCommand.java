package dev.quasemago.aura.client.app.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

public interface SlashCommand {
    Mono<Void> execute(ChatInputInteractionEvent event, User author);

    String getName();

    String getDescription();

    default Permission getPermission() {
        return Permission.SEND_MESSAGES;
    }

    ApplicationCommandRequest getCommand();

    CommandCategory getCategory();
}