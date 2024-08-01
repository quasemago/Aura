package dev.quasemago.aura.client.interfaces.commands.handler.general;

import dev.quasemago.aura.client.interfaces.commands.CommandCategory;
import dev.quasemago.aura.client.interfaces.commands.AbstractSlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PingCommand extends AbstractSlashCommand {
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
