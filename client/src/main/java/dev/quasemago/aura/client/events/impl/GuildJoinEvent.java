package dev.quasemago.aura.client.events.impl;

import dev.quasemago.aura.client.events.AbstractEventListener;
import dev.quasemago.aura.client.service.ServerGuildService;
import dev.quasemago.aura.client.util.Logger;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class GuildJoinEvent extends AbstractEventListener<GuildCreateEvent> {
    private final ServerGuildService guildService;

    public GuildJoinEvent(ServerGuildService guildService) {
        this.guildService = guildService;
    }

    @Override
    public Mono<Void> execute(GuildCreateEvent event) {
        return Mono.just(event)
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(e -> {
                    Logger.log.info("Joined guild: {}", e.getGuild().getName());
                    guildService.findOrCreateGuild(e.getGuild().getId().asLong());
                })
                .then();
    }
}
