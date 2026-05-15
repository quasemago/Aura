package dev.quasemago.aura.shared;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class BotRuntime {
    private final Instant startedAt = Instant.now();

    public Duration uptime() {
        return Duration.between(startedAt, Instant.now());
    }

    public String formattedUptime() {
        var uptime = uptime();
        var days = uptime.toDays();
        var hours = uptime.toHoursPart();
        var minutes = uptime.toMinutesPart();
        var seconds = uptime.toSecondsPart();

        if (days > 0) {
            return "%dd %02dh %02dm %02ds".formatted(days, hours, minutes, seconds);
        }
        return "%02dh %02dm %02ds".formatted(hours, minutes, seconds);
    }
}
