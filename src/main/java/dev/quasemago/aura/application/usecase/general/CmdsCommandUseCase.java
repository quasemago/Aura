package dev.quasemago.aura.application.usecase.general;

import dev.quasemago.aura.infrastructure.discord.DiscordPermissionService;
import dev.quasemago.aura.infrastructure.i18n.TranslationService;
import dev.quasemago.aura.interfaces.discord.command.AbstractSlashCommand;
import dev.quasemago.aura.interfaces.discord.command.CommandCategory;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CmdsCommandUseCase {
    private final ObjectProvider<AbstractSlashCommand> commands;
    private final GatewayDiscordClient gatewayClient;
    private final TranslationService translate;
    private final DiscordPermissionService permissionService;

    public CmdsCommandUseCase(
            ObjectProvider<AbstractSlashCommand> commands,
            GatewayDiscordClient gatewayClient,
            TranslationService translate,
            DiscordPermissionService permissionService
    ) {
        this.commands = commands;
        this.gatewayClient = gatewayClient;
        this.translate = translate;
        this.permissionService = permissionService;
    }

    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        var availableCommands = commands.orderedStream()
                .filter(command -> !command.isHidden())
                .sorted(Comparator.comparing(AbstractSlashCommand::getName))
                .toList();

        return Flux.fromIterable(availableCommands)
                .flatMap(command -> permissionService.canExecute(event, command)
                        .filter(Boolean::booleanValue)
                        .map(ignored -> command))
                .collectList()
                .flatMap(allowedCommands -> {
                    var commandsByCategory = allowedCommands.stream()
                            .collect(Collectors.groupingBy(AbstractSlashCommand::getCategory));

                    if (commandsByCategory.isEmpty()) {
                        return event.reply(translate.t("CMD_CMDS_NO_COMMANDS"))
                                .withEphemeral(true);
                    }

                    return gatewayClient.getSelf()
                            .flatMap(bot -> {
                                var embed = EmbedCreateSpec.builder()
                                        .title("🤖 " + translate.t("CMD_CMDS_TITLE", Map.of("botName", bot.getUsername())))
                                        .description(translate.t("CMD_CMDS_DESCRIPTION_TEXT"))
                                        .color(Color.of(0x0099ff))
                                        .thumbnail(bot.getAvatarUrl())
                                        .timestamp(Instant.now())
                                        .footer(translate.t("COMMON_REQUESTED_BY", Map.of("username", author.getUsername())), author.getAvatarUrl());

                                addCategoryFields(embed, commandsByCategory);

                                return event.reply()
                                        .withEmbeds(embed.build())
                                        .withEphemeral(true);
                            });
                });
    }

    private void addCategoryFields(
            EmbedCreateSpec.Builder embed,
            Map<CommandCategory, List<AbstractSlashCommand>> commandsByCategory
    ) {
        commandsByCategory.forEach((category, categoryCommands) -> {
            var commandNames = categoryCommands.stream()
                    .map(command -> "- `/%s` - %s".formatted(command.getName(), command.getDescription()))
                    .collect(Collectors.joining("\n"));

            embed.addField(
                    translate.t(category.getTranslationKey()),
                    commandNames.isBlank() ? translate.t("CMD_CMDS_NO_COMMANDS_IN_CATEGORY") : commandNames,
                    false
            );
        });
    }
}
