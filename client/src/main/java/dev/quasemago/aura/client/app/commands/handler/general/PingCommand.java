package dev.quasemago.aura.client.app.commands.handler.general;

import dev.quasemago.aura.client.app.commands.CommandCategory;
import dev.quasemago.aura.client.app.commands.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PingCommand implements SlashCommand {
    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        return event.reply("Pong!");
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Ping Pong";
    }

    @Override
    public ApplicationCommandRequest getCommand() {
        return ApplicationCommandRequest.builder()
                .name(getName())
                .description(getDescription())
                .dmPermission(false)
                .defaultMemberPermissions(String.valueOf(getPermission().getValue()))
                .build();
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERAL;
    }


}
