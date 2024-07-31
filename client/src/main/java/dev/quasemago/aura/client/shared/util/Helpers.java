package dev.quasemago.aura.client.shared.util;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.rest.util.Permission;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Objects;

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
}
