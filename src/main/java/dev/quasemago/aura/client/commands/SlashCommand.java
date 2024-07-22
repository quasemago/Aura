package dev.quasemago.aura.client.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

public interface SlashCommand {
    Mono<Void> execute(ChatInputInteractionEvent event);

    String name();

    String description();

    Permission permission();

    ApplicationCommandRequest getCommand();
}