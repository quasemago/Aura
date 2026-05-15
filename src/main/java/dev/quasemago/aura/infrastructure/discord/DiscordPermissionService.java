package dev.quasemago.aura.infrastructure.discord;

import dev.quasemago.aura.interfaces.discord.command.AbstractSlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.rest.util.Permission;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DiscordPermissionService {

    public Mono<Boolean> canExecute(ChatInputInteractionEvent event, AbstractSlashCommand command) {
        var requiredPermissions = command.getRequiredPermissions();
        if (requiredPermissions.getRawValue() == 0L) {
            return Mono.just(true);
        }

        if (event.getInteraction().getGuildId().isEmpty()) {
            return Mono.just(true);
        }

        var author = event.getInteraction().getUser();
        return event.getInteraction()
                .getGuild()
                .flatMap(guild -> guild.getMemberById(author.getId()))
                .flatMap(member -> member.getBasePermissions())
                .map(memberPermissions -> memberPermissions.contains(Permission.ADMINISTRATOR)
                        || memberPermissions.containsAll(requiredPermissions))
                .defaultIfEmpty(false)
                .onErrorReturn(false);
    }
}
