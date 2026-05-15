package dev.quasemago.aura.interfaces.discord.command.general;

import dev.quasemago.aura.application.usecase.general.CmdsCommandUseCase;
import dev.quasemago.aura.infrastructure.i18n.TranslationService;
import dev.quasemago.aura.interfaces.discord.command.AbstractSlashCommand;
import dev.quasemago.aura.interfaces.discord.command.CommandCategory;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CmdsCommand extends AbstractSlashCommand {
    private final CmdsCommandUseCase useCase;
    private final TranslationService translate;

    public CmdsCommand(CmdsCommandUseCase useCase, TranslationService translate) {
        this.useCase = useCase;
        this.translate = translate;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        return useCase.execute(event, author);
    }

    @Override
    public String getName() {
        return translate.t("CMD_CMDS_NAME");
    }

    @Override
    public String getDescription() {
        return translate.t("CMD_CMDS_DESCRIPTION");
    }

    @Override
    public ApplicationCommandRequest getCommand() {
        return ApplicationCommandRequest.builder()
                .name(getName())
                .description(getDescription())
                .build();
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERAL;
    }
}
