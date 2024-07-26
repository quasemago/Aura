package dev.quasemago.aura.client.app.commands.handler.general;

import dev.quasemago.aura.client.app.commands.CommandCategory;
import dev.quasemago.aura.client.app.commands.SlashCommand;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import discord4j.rest.util.Permission;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CmdsCommand implements SlashCommand {
    private final Map<String, SlashCommand> commandMap;
    private final GatewayDiscordClient gatewayClient;

    public CmdsCommand(List<SlashCommand> commandList,
                       GatewayDiscordClient gatewayClient) {
        this.commandMap = commandList.stream()
                .collect(Collectors.toMap(SlashCommand::name, command -> command));
        this.gatewayClient = gatewayClient;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        final var commandsByCategory = getCommandsByCategory();
        if (commandsByCategory.isEmpty()) {
            return event.reply("No commands found.")
                    .withEphemeral(true);
        }
        return gatewayClient.getSelf()
                .flatMap(bot -> {
                    final var botName = bot.getUsername();
                    final var botAvatar = bot.getAvatarUrl();

                    final var embed = EmbedCreateSpec.builder()
                            .title("\uD83E\uDD16 " + botName + " - Command List")
                            .thumbnail(botAvatar)
                            .color(Color.of(0x0099ff))
                            .timestamp(Instant.now())
                            .footer("Requested by " + author.getUsername(), author.getAvatarUrl());

                    commandsByCategory.forEach((category, commands) -> {
                        final var commandList = commands.stream()
                                .map(command -> String.format("- `/%s` - %s", command.name(), command.description()))
                                .collect(Collectors.joining("\n"));
                        embed.addField("**" + category.getName() + ":**", commandList, false);
                    });

                    return event.reply()
                            .withEmbeds(embed.build())
                            .withEphemeral(true);
                });
    }

    private Map<CommandCategory, List<SlashCommand>> getCommandsByCategory() {
        return commandMap.values().stream()
                .collect(Collectors.groupingBy(SlashCommand::getCategory));
    }

    @Override
    public String name() {
        return "cmds";
    }

    @Override
    public String description() {
        return "View the command list";
    }

    @Override
    public Permission permission() {
        return Permission.SEND_MESSAGES;
    }

    @Override
    public ApplicationCommandRequest getCommand() {
        return ApplicationCommandRequest.builder()
                .name(name())
                .description(description())
                .dmPermission(false)
                .defaultMemberPermissions(String.valueOf(permission().getValue()))
                .build();
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERAL;
    }
}
