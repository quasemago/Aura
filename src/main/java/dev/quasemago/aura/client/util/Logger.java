package dev.quasemago.aura.client.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Logger {
    public static org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);
}
