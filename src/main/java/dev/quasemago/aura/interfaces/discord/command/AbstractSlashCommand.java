package dev.quasemago.aura.interfaces.discord.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

public abstract class AbstractSlashCommand {

    public abstract Mono<Void> execute(ChatInputInteractionEvent event, User author);

    public abstract String getName();

    public abstract String getDescription();

    public abstract ApplicationCommandRequest getCommand();

    public abstract CommandCategory getCategory();

    public boolean isHidden() {
        return false;
    }
}
