package dev.quasemago.aura.client.app.service;

import dev.quasemago.aura.client.shared.util.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KeepAliveService {
    @Scheduled(fixedRate = 60000)
    public void keepAlive() {
        Logger.log.debug("Keeping alive...");
    }
}
