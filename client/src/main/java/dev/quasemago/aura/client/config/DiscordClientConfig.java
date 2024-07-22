package dev.quasemago.aura.client.config;

import dev.quasemago.aura.client.util.Logger;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.gateway.GatewayReactorResources;
import discord4j.gateway.intent.IntentSet;
import discord4j.rest.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.Objects;

@Configuration
public class DiscordClientConfig {
    @Value("${discord.bot-id}")
    private String botId;
    @Value("${discord.bot-token}")
    private String botToken;
    @Value("${discord.bot-presence-type}")
    private Integer botPresenceType;
    @Value("${discord.bot-presence-msg}")
    private String botPresenceMsg;

    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {
        final var gateway = DiscordClient.create(botToken)
                .gateway()
                .setEnabledIntents(IntentSet.all())
                .setInitialPresence(shard -> ClientPresence
                        .online(ClientActivity.of(Activity.Type.of(botPresenceType), botPresenceMsg, null)))
                .setGatewayReactorResources(reactorResources -> GatewayReactorResources.builder(reactorResources)
                        .httpClient(HttpClient.create(ConnectionProvider.newConnection())
                                .compress(true)
                                .followRedirect(true)
                                .secure())
                        .build())
                .login()
                .block();

        if (Objects.isNull(gateway)) {
            Logger.log.error("Failed to login to Discord Gateway");
            System.exit(0);
        }

        return gateway;
    }

    @Bean
    public RestClient discordRestClient(final GatewayDiscordClient client) {
        return client.getRestClient();
    }
}
