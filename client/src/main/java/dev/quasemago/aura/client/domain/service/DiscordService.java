package dev.quasemago.aura.client.domain.service;

import dev.quasemago.aura.client.app.commands.SlashCommand;
import dev.quasemago.aura.client.app.events.GenericEventListener;
import dev.quasemago.aura.client.shared.util.Logger;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiscordService {
    private final RestClient client;
    private final GatewayDiscordClient gatewayClient;
    private final List<SlashCommand> commandList;
    private final List<GenericEventListener<? extends Event>> eventList;

    public DiscordService(RestClient client,
                          GatewayDiscordClient gatewayClient,
                          List<SlashCommand> commandList,
                          List<GenericEventListener<? extends Event>> eventList) {
        this.client = client;
        this.gatewayClient = gatewayClient;
        this.commandList = commandList;
        this.eventList = eventList;
    }

    @PostConstruct
    public void setupDiscord() {
        registerCommands();
        registerEvents();
    }

    private void registerCommands() {
        final List<ApplicationCommandRequest> cmdRequests = new ArrayList<>();

        for (SlashCommand cmd : commandList) {
            cmdRequests.add(cmd.getCommand());
        }

        final var applicationId = client.getApplicationId().block();
        client.getApplicationService()
                .bulkOverwriteGlobalApplicationCommand(applicationId, cmdRequests)
                .doOnNext(cmd -> Logger.log.info("Registered command: {}", cmd.name()))
                .doOnError(err -> Logger.log.error("Error while registering command: {}", err.getMessage()))
                .subscribe();
    }

    private void registerEvents() {
        for (GenericEventListener<? extends Event> listener : eventList) {
            registerEventListener(listener);
        }
    }

    private <T extends Event> void registerEventListener(GenericEventListener<T> listener) {
        gatewayClient.on(listener.getEventType())
                .flatMap(event -> listener.execute(event)
                        .onErrorResume(listener::onError))
                .subscribe();
    }
}
