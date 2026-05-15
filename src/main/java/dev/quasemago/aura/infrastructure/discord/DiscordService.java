package dev.quasemago.aura.infrastructure.discord;

import dev.quasemago.aura.infrastructure.discord.events.GenericEventListener;
import dev.quasemago.aura.interfaces.discord.command.AbstractSlashCommand;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscordService {
    private static final Logger log = LoggerFactory.getLogger(DiscordService.class);

    private final RestClient restClient;
    private final GatewayDiscordClient gatewayClient;
    private final List<AbstractSlashCommand> commands;
    private final List<GenericEventListener<? extends Event>> events;

    public DiscordService(
            RestClient restClient,
            GatewayDiscordClient gatewayClient,
            List<AbstractSlashCommand> commands,
            List<GenericEventListener<? extends Event>> events
    ) {
        this.restClient = restClient;
        this.gatewayClient = gatewayClient;
        this.commands = commands;
        this.events = events;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        registerReadyEvent();
        registerEvents();
        registerCommands();
    }

    private void registerReadyEvent() {
        gatewayClient.on(ReadyEvent.class)
                .doOnNext(event -> log.info("Logged in as {}!", event.getSelf().getTag()))
                .subscribe();
    }

    private void registerEvents() {
        for (GenericEventListener<? extends Event> listener : events) {
            registerEventListener(listener);
        }
    }

    private <T extends Event> void registerEventListener(GenericEventListener<T> listener) {
        gatewayClient.on(listener.getEventType())
                .flatMap(event -> listener.execute(event)
                        .onErrorResume(listener::onError))
                .subscribe();
    }

    private void registerCommands() {
        List<ApplicationCommandRequest> commandRequests = commands.stream()
                .map(AbstractSlashCommand::getCommand)
                .toList();

        var applicationId = restClient.getApplicationId().block();

        restClient.getApplicationService()
                .bulkOverwriteGlobalApplicationCommand(applicationId, commandRequests)
                .doOnNext(command -> log.info("Registered command: {}", command.name()))
                .doOnError(error -> log.error("Error while registering Discord commands", error))
                .subscribe();
    }

    public List<AbstractSlashCommand> getCommands() {
        return commands;
    }

    @Scheduled(fixedRate = 60000)
    public void keepAlive() {
        log.debug("Keeping alive...");
    }

    @PreDestroy
    public void stop() {
        gatewayClient.logout().block();
    }
}
