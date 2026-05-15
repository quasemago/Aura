package dev.quasemago.aura.application.usecase.general;

import dev.quasemago.aura.infrastructure.i18n.TranslationService;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.gateway.GatewayClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
public class PingCommandUseCase {
    private final GatewayDiscordClient gatewayClient;
    private final TranslationService translate;

    public PingCommandUseCase(GatewayDiscordClient gatewayClient, TranslationService translate) {
        this.gatewayClient = gatewayClient;
        this.translate = translate;
    }

    public Mono<Void> execute(ChatInputInteractionEvent event) {
        var ping = gatewayClient.getGatewayClientGroup()
                .find(0)
                .map(GatewayClient::getResponseTime)
                .map(Duration::toMillis)
                .orElse(0L);

        return event.reply(translate.t("CMD_PING_RESPONSE", Map.of("ping", ping)))
                .withEphemeral(true);
    }
}
