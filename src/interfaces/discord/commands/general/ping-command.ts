import { PingCommandUseCase } from "@/application/usecases/general/ping-command-usecase";
import { TranslationService } from "@/infrastructure/i18n/translation-service";
import { DiscordGuildCommandCategory } from "@/interfaces/discord/types/i-command";
import type { ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseCommand } from "../base-command";

@injectable()
export class PingCommand extends BaseCommand {
  constructor(
    @inject(PingCommandUseCase) private readonly pingCommandUseCase: PingCommandUseCase,
    @inject(TranslationService) translate: TranslationService
  ) {
    super({
      name: translate.t("CMD_PING_NAME"),
      description: translate.t("CMD_PING_DESCRIPTION"),
      category: DiscordGuildCommandCategory.GENERAL
    });
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.pingCommandUseCase.execute(interaction);
  }
}
