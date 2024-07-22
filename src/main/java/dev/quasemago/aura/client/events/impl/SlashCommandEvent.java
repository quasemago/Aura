package dev.quasemago.aura.client.events.impl;

import dev.quasemago.aura.client.commands.SlashCommand;
import dev.quasemago.aura.client.events.AbstractEventListener;
import dev.quasemago.aura.client.util.Logger;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class SlashCommandEvent extends AbstractEventListener<ChatInputInteractionEvent> {
    private final List<SlashCommand> commandList;

    public SlashCommandEvent(List<SlashCommand> commandList) {
        this.commandList = commandList;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        final var command = commandList.stream()
                .filter(c -> c.name().equals(event.getCommandName()))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(command)) {
            Logger.log.error("Command not found: {}", event.getCommandName());
            return event.createFollowup("Slash command " + event.getCommandName() + " not found.")
                    .withEphemeral(true)
                    .then();
        }

        try {
            // TODO: Check user permissions.
            return command.execute(event);
        } catch (Exception e) {
            Logger.log.error("Error while executing slash command {}: {}", event.getCommandName(), e.getMessage());
            return event.createFollowup("Error while executing slash command " + event.getCommandName())
                    .withEphemeral(true)
                    .then();
        }
    }
}