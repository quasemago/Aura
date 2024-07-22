package dev.quasemago.aura.client.events;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

public interface GenericEventListener<T extends Event> {
    Class<T> getEventType();

    Mono<Void> execute(T event);

    default Mono<Void> onError(Throwable error) {
        return Mono.empty();
    }
}
