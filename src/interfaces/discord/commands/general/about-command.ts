import { AboutCommandUseCase } from "@/application/usecases/general/about-command-usecase";
import { TranslationService } from "@/infrastructure/i18n/translation-service";
import { DiscordGuildCommandCategory } from "@/interfaces/discord/types/i-command";
import type { ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseCommand } from "../base-command";

@injectable()
export class AboutCommand extends BaseCommand {
  constructor(
    @inject(AboutCommandUseCase) private readonly aboutCommandUseCase: AboutCommandUseCase,
    @inject(TranslationService) translate: TranslationService
  ) {
    super({
      name: translate.t("CMD_ABOUT_NAME"),
      description: translate.t("CMD_ABOUT_DESCRIPTION"),
      category: DiscordGuildCommandCategory.GENERAL
    });
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.aboutCommandUseCase.execute(interaction);
  }
}
