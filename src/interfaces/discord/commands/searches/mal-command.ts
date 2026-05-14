import { MalCommandUseCase } from "@/application/usecases/searches/mal-command-usecase";
import { TranslationService } from "@/infrastructure/i18n/translation-service";
import { DiscordGuildCommandCategory } from "@/interfaces/discord/types/i-command";
import { InteractionContextType, type ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseCommand } from "../base-command";

@injectable()
export class MalCommand extends BaseCommand {
  constructor(
    @inject(MalCommandUseCase) private readonly malCommandUseCase: MalCommandUseCase,
    @inject(TranslationService) translate: TranslationService
  ) {
    super({
      name: translate.t("CMD_MAL_NAME"),
      description: translate.t("CMD_MAL_DESCRIPTION"),
      category: DiscordGuildCommandCategory.SEARCHES,
      configure: (builder) => {
        builder
          .setContexts(InteractionContextType.Guild)
          .addStringOption((option) =>
            option
              .setName(translate.t("CMD_MAL_OPTION_NAME_NAME"))
              .setDescription(translate.t("CMD_MAL_OPTION_NAME_DESCRIPTION"))
              .setRequired(true)
          );
      }
    });
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.malCommandUseCase.execute(interaction);
  }
}
