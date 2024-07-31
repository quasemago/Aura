package dev.quasemago.aura.client.app.events.handler;

import dev.quasemago.aura.client.app.commands.SlashCommand;
import dev.quasemago.aura.client.app.events.AbstractEventListener;
import dev.quasemago.aura.client.shared.util.DiscordHelpers;
import dev.quasemago.aura.client.shared.util.Logger;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class SlashCommandEventHandler extends AbstractEventListener<ChatInputInteractionEvent> {
    private final List<SlashCommand> commandList;

    public SlashCommandEventHandler(List<SlashCommand> commandList) {
        this.commandList = commandList;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        final var command = commandList.stream()
                .filter(c -> c.getName().equals(event.getCommandName()))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(command)) {
            Logger.log.error("Command not found: {}", event.getCommandName());
            return event.createFollowup("Slash command " + event.getCommandName() + " not found.")
                    .withEphemeral(true)
                    .then();
        }

        final var eventInteraction = event.getInteraction();
        final var author = eventInteraction.getUser();

        return DiscordHelpers.userHasPermission(eventInteraction.getGuild(), author, command.getPermission())
                .flatMap(hasPermission -> {
                    if (hasPermission) {
                        return command.execute(event, author);
                    } else {
                        return event.deferReply()
                                .then(event.createFollowup("You don't have getPermission to use this command.")
                                        .withEphemeral(true)
                                        .then());
                    }
                })
                .onErrorResume(err -> {
                    Logger.log.error("Error while executing slash command {}: {}", event.getCommandName(), err.getMessage());
                    return event.deferReply()
                            .then(event.createFollowup("An error occurred while executing the command.")
                                    .withEphemeral(true)
                                    .then());
                });
    }
}