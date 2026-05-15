package dev.quasemago.aura.shared;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import dev.quasemago.aura.infrastructure.jikan.dto.NamedResource;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class DiscordHelpers {
    private DiscordHelpers() {
    }

    public static Mono<ApplicationCommandInteractionOptionValue> getRequiredOption(
            ChatInputInteractionEvent event,
            String optionName
    ) {
        return Mono.justOrEmpty(event.getInteraction()
                        .getCommandInteraction()
                        .flatMap(interaction -> interaction.getOption(optionName))
                        .flatMap(ApplicationCommandInteractionOption::getValue))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(optionName + " option is required")));
    }

    public static String valueOrDefault(Object value, String defaultValue) {
        return Optional.ofNullable(value).map(Object::toString).filter(s -> !s.isBlank()).orElse(defaultValue);
    }

    public static String truncate(String value, int maxLength, String defaultValue) {
        var safeValue = valueOrDefault(value, defaultValue);
        if (safeValue.length() <= maxLength) {
            return safeValue;
        }
        return safeValue.substring(0, maxLength) + "...";
    }

    public static String joinNames(List<NamedResource> resources, String defaultValue) {
        if (resources == null || resources.isEmpty()) {
            return defaultValue;
        }

        var joined = resources.stream()
                .filter(Objects::nonNull)
                .map(NamedResource::name)
                .filter(Objects::nonNull)
                .filter(name -> !name.isBlank())
                .collect(Collectors.joining(", "));

        return joined.isBlank() ? defaultValue : joined;
    }
}
