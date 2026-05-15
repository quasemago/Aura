package dev.quasemago.aura.interfaces.discord.command.searches;

import dev.quasemago.aura.application.usecase.searches.MalCommandUseCase;
import dev.quasemago.aura.infrastructure.i18n.TranslationService;
import dev.quasemago.aura.interfaces.discord.command.AbstractSlashCommand;
import dev.quasemago.aura.interfaces.discord.command.CommandCategory;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MalCommand extends AbstractSlashCommand {
    private final MalCommandUseCase useCase;
    private final TranslationService translate;

    public MalCommand(MalCommandUseCase useCase, TranslationService translate) {
        this.useCase = useCase;
        this.translate = translate;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event, User author) {
        return useCase.execute(event, author);
    }

    @Override
    public String getName() {
        return translate.t("CMD_MAL_NAME");
    }

    @Override
    public String getDescription() {
        return translate.t("CMD_MAL_DESCRIPTION");
    }

    @Override
    public ApplicationCommandRequest getCommand() {
        return commandBuilder()
                .addOption(ApplicationCommandOptionData.builder()
                        .name(translate.t("CMD_MAL_OPTION_NAME_NAME"))
                        .description(translate.t("CMD_MAL_OPTION_NAME_DESCRIPTION"))
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .build();
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.SEARCHES;
    }
}
