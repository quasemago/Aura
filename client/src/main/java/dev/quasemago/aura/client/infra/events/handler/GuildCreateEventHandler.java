package dev.quasemago.aura.client.infra.events.handler;

import dev.quasemago.aura.client.infra.events.AbstractEventListener;
import dev.quasemago.aura.client.app.service.ServerGuildService;
import dev.quasemago.aura.client.shared.util.Logger;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class GuildCreateEventHandler extends AbstractEventListener<GuildCreateEvent> {
    private final ServerGuildService guildService;

    public GuildCreateEventHandler(ServerGuildService guildService) {
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
