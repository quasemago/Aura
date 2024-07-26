package dev.quasemago.aura.client.app.commands.handler.general;

import dev.quasemago.aura.client.app.commands.CommandCategory;
import dev.quasemago.aura.client.app.commands.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Permission;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PingCommand implements SlashCommand {
    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        return event.reply("Pong!");
    }

    @Override
    public String name() {
        return "ping";
    }

    @Override
    public String description() {
        return "Ping Pong";
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
