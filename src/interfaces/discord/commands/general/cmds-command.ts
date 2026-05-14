import { CmdsCommandUseCase } from "@/application/usecases/general/cmds-command-usecase";
import { TranslationService } from "@/infrastructure/i18n/translation-service";
import { DiscordGuildCommandCategory } from "@/interfaces/discord/types/i-command";
import { type ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseCommand } from "../base-command";

@injectable()
export class CmdsCommand extends BaseCommand {
  constructor(
    @inject(CmdsCommandUseCase) private readonly cmdsCommandUseCase: CmdsCommandUseCase,
    @inject(TranslationService) translate: TranslationService
  ) {
    super({
      name: translate.t("CMD_CMDS_NAME"),
      description: translate.t("CMD_CMDS_DESCRIPTION"),
      category: DiscordGuildCommandCategory.GENERAL
    });
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.cmdsCommandUseCase.execute(interaction);
  }
}
