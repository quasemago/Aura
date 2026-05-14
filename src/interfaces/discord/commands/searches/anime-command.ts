import { AnimeCommandUseCase } from "@/application/usecases/searches/anime-command-usecase";
import { TranslationService } from "@/infrastructure/i18n/translation-service";
import { DiscordGuildCommandCategory } from "@/interfaces/discord/types/i-command";
import { InteractionContextType, type ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseCommand } from "../base-command";

@injectable()
export class AnimeCommand extends BaseCommand {
  constructor(
    @inject(AnimeCommandUseCase) private readonly animeCommandUseCase: AnimeCommandUseCase,
    @inject(TranslationService) translate: TranslationService
  ) {
    super({
      name: translate.t("CMD_ANIME_NAME"),
      description: translate.t("CMD_ANIME_DESCRIPTION"),
      category: DiscordGuildCommandCategory.SEARCHES,
      configure: (builder) => {
        builder
          .setContexts(InteractionContextType.Guild)
          .addStringOption((option) =>
            option
              .setName(translate.t("CMD_ANIME_OPTION_TITLE_NAME"))
              .setDescription(translate.t("CMD_ANIME_OPTION_TITLE_DESCRIPTION"))
              .setRequired(true)
          );
      }
    });
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.animeCommandUseCase.execute(interaction);
  }
}
