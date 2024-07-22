package dev.quasemago.aura.client.data.repository;

import dev.quasemago.aura.client.data.model.ServerGuild;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServerGuildRepository extends JpaRepository<ServerGuild, String> {
    @Nonnull
    Optional<ServerGuild> findById(@Nonnull String id);

    Optional<ServerGuild> findByGuildId(Long guildId);
}

