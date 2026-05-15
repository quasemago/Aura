package dev.quasemago.aura.application.usecase.general;

import dev.quasemago.aura.config.AuraProperties;
import dev.quasemago.aura.infrastructure.i18n.TranslationService;
import dev.quasemago.aura.shared.BotRuntime;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@Service
public class AboutCommandUseCase {
    private final GatewayDiscordClient gatewayClient;
    private final AuraProperties properties;
    private final TranslationService translate;
    private final BotRuntime botRuntime;

    public AboutCommandUseCase(
            GatewayDiscordClient gatewayClient,
            AuraProperties properties,
            TranslationService translate,
            BotRuntime botRuntime
    ) {
        this.gatewayClient = gatewayClient;
        this.properties = properties;
        this.translate = translate;
        this.botRuntime = botRuntime;
    }

    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        return gatewayClient.getSelf()
                .flatMap(bot -> {
                    var embed = EmbedCreateSpec.builder()
                            .title("🤖 " + bot.getUsername())
                            .description(translate.t("CMD_ABOUT_DESCRIPTION_TEXT"))
                            .color(Color.of(0x0099ff))
                            .thumbnail(bot.getAvatarUrl())
                            .timestamp(Instant.now())
                            .addField(translate.t("CMD_ABOUT_FIELD_CREATED_BY"), "quasemago [(Github)](https://github.com/quasemago)", true)
                            .addField(translate.t("CMD_ABOUT_FIELD_VERSION"), properties.getBot().getVersion(), true)
                            .addField(translate.t("CMD_ABOUT_FIELD_UPTIME"), botRuntime.formattedUptime(), true)
                            .footer(translate.t("COMMON_REQUESTED_BY", Map.of("username", author.getUsername())), author.getAvatarUrl())
                            .build();

                    return event.reply()
                            .withEmbeds(embed)
                            .withEphemeral(true);
                });
    }
}
