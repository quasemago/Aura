package dev.quasemago.aura.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class AuraClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuraClientApplication.class, args);
    }
}
