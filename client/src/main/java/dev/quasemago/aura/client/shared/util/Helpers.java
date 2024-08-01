package dev.quasemago.aura.client.shared.util;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.rest.util.Permission;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Helpers {
    public static Mono<Boolean> discordUserHasPermission(Mono<Guild> guild, User user, Permission permission) {
        if (Objects.isNull(user) || Objects.isNull(guild)) {
            return Mono.just(false);
        }
        return guild.flatMap(g -> g.getMemberById(user.getId()))
                .flatMap(member -> member.getBasePermissions())
                .map(permissions -> permissions.contains(permission))
                .defaultIfEmpty(false)
                .onErrorReturn(false);
    }

    public static Mono<ApplicationCommandInteractionOptionValue> getDiscordCommandArgValue(ChatInputInteractionEvent event,
                                                                                           String fieldName) {
        return Mono.justOrEmpty(event.getInteraction()
                        .getCommandInteraction()
                        .flatMap(interaction -> interaction.getOption(fieldName))
                        .flatMap(ApplicationCommandInteractionOption::getValue))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(fieldName + " field is required!")));
    }

    public static String joinStringList(List<String> values, String delimiter, String defaultValue) {
        return values == null || values.isEmpty() ? defaultValue : String.join(delimiter, values);
    }

    public static String convertObjectToString(Object obj, String defaultValue) {
        return Optional.ofNullable(obj).map(Object::toString).orElse(defaultValue);
    }
}
