package dev.quasemago.aura.infrastructure.jikan;

import dev.quasemago.aura.config.AuraProperties;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class JikanFeignConfig {

    @Bean
    public RequestInterceptor userAgentInterceptor(AuraProperties properties) {
        return template -> template.header("User-Agent", properties.getJikan().getUserAgent());
    }
}
