package dev.quasemago.aura.client.app.service;

import dev.quasemago.aura.client.domain.model.ServerGuild;
import dev.quasemago.aura.client.domain.repository.ServerGuildRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServerGuildService {
    private final ServerGuildRepository repository;

    public ServerGuildService(ServerGuildRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ServerGuild createServerGuild(ServerGuild guild) {
        return repository.save(guild);
    }

    @Transactional
    public ServerGuild findOrCreateGuild(Long guildId) {
        return repository.findByGuildId(guildId)
                .orElseGet(() -> createServerGuild(new ServerGuild(guildId)));
    }

    @Transactional
    public void deleteGuild(Long guildId) {
        repository.deleteByGuildId(guildId);
    }
}