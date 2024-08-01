package dev.quasemago.aura.client.infra.events;

import discord4j.core.event.domain.Event;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractEventListener<T extends Event> implements GenericEventListener<T> {
    private final Class<T> eventType;

    @SuppressWarnings("unchecked")
    public AbstractEventListener() {
        this.eventType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Class<T> getEventType() {
        return this.eventType;
    }
}