package dev.quasemago.aura;

import dev.quasemago.aura.config.AuraProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(AuraProperties.class)
public class AuraApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuraApplication.class, args);
    }
}
