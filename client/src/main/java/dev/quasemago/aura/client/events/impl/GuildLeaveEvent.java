package dev.quasemago.aura.client.events.impl;

import dev.quasemago.aura.client.events.AbstractEventListener;
import dev.quasemago.aura.client.service.ServerGuildService;
import dev.quasemago.aura.client.util.Logger;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class GuildLeaveEvent extends AbstractEventListener<GuildDeleteEvent> {
    private final ServerGuildService guildService;

    public GuildLeaveEvent(ServerGuildService guildService) {
        this.guildService = guildService;
    }

    @Override
    public Mono<Void> execute(GuildDeleteEvent event) {
        return Mono.just(event)
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(e -> {
                    Logger.log.info("Leaving guild: {}", e.getGuildId().asLong());
                    guildService.deleteGuild(e.getGuildId().asLong());
                })
                .then();
    }
}
