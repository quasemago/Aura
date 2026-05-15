package dev.quasemago.aura.infrastructure.discord.events;

import dev.quasemago.aura.infrastructure.discord.DiscordPermissionService;
import dev.quasemago.aura.infrastructure.i18n.TranslationService;
import dev.quasemago.aura.interfaces.discord.command.AbstractSlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SlashCommandEventHandler extends AbstractEventListener<ChatInputInteractionEvent> {
    private static final Logger log = LoggerFactory.getLogger(SlashCommandEventHandler.class);

    private final List<AbstractSlashCommand> commands;
    private final TranslationService translate;
    private final DiscordPermissionService permissionService;

    public SlashCommandEventHandler(
            List<AbstractSlashCommand> commands,
            TranslationService translate,
            DiscordPermissionService permissionService
    ) {
        this.commands = commands;
        this.translate = translate;
        this.permissionService = permissionService;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        var command = commands.stream()
                .filter(candidate -> candidate.getName().equals(event.getCommandName()))
                .findFirst();

        if (command.isEmpty()) {
            log.error("No command matching {} was found.", event.getCommandName());
            return event.reply("Slash command %s not found.".formatted(event.getCommandName()))
                    .withEphemeral(true)
                    .then();
        }

        var author = event.getInteraction().getUser();
        return permissionService.canExecute(event, command.get())
                .flatMap(canExecute -> {
                    if (!canExecute) {
                        return event.reply(translate.t("DISCORD_COMMAND_ERROR_PERMISSION"))
                                .withEphemeral(true)
                                .then();
                    }
                    return command.get().execute(event, author);
                })
                .onErrorResume(error -> {
                    log.error("Error while executing slash command {}", event.getCommandName(), error);
                    return event.reply(translate.t("DISCORD_COMMAND_ERROR_EXECUTION"))
                            .withEphemeral(true)
                            .onErrorResume(replyError -> event.createFollowup(translate.t("DISCORD_COMMAND_ERROR_EXECUTION"))
                                    .withEphemeral(true)
                                    .then());
                });
    }
}
