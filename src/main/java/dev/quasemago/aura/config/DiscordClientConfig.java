package dev.quasemago.aura.config;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.gateway.GatewayReactorResources;
import discord4j.gateway.intent.IntentSet;
import discord4j.rest.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class DiscordClientConfig {
    private static final Logger log = LoggerFactory.getLogger(DiscordClientConfig.class);

    @Bean
    public GatewayDiscordClient gatewayDiscordClient(AuraProperties properties) {
        var bot = properties.getBot();

        log.info("Logging in to Discord gateway...");
        var gateway = DiscordClient.create(bot.getToken())
                .gateway()
                .setEnabledIntents(IntentSet.all())
                .setInitialPresence(shard -> ClientPresence.online(ClientActivity.of(
                        Activity.Type.of(bot.getPresenceType()),
                        bot.getPresenceMessage(),
                        null
                )))
                .setGatewayReactorResources(reactorResources -> GatewayReactorResources.builder(reactorResources)
                        .httpClient(HttpClient.create(ConnectionProvider.newConnection())
                                .compress(true)
                                .followRedirect(true)
                                .secure())
                        .build())
                .login()
                .block();

        if (gateway == null) {
            throw new IllegalStateException("Failed to login to Discord gateway");
        }

        return gateway;
    }

    @Bean
    public RestClient discordRestClient(GatewayDiscordClient gatewayDiscordClient) {
        return gatewayDiscordClient.getRestClient();
    }
}
