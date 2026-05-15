package dev.quasemago.aura.interfaces.discord.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Mono;

public abstract class AbstractSlashCommand {

    public abstract Mono<Void> execute(ChatInputInteractionEvent event, User author);

    public abstract String getName();

    public abstract String getDescription();

    public abstract ApplicationCommandRequest getCommand();

    public abstract CommandCategory getCategory();

    public PermissionSet getRequiredPermissions() {
        return PermissionSet.none();
    }

    protected ImmutableApplicationCommandRequest.Builder commandBuilder() {
        var builder = ApplicationCommandRequest.builder()
                .name(getName())
                .description(getDescription());

        var permissions = getRequiredPermissions();
        if (permissions.getRawValue() != 0L) {
            builder.defaultMemberPermissions(String.valueOf(permissions.getRawValue()));
        }

        return builder;
    }

    public boolean isHidden() {
        return false;
    }
}
