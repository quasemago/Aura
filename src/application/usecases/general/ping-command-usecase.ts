import { TranslationService } from "@/infrastructure/i18n/translation-service";
import { type ChatInputCommandInteraction, MessageFlags } from "discord.js";
import { inject, injectable } from "inversify";
import { AbstractBaseUseCase } from "../base-usecase";

@injectable()
export class PingCommandUseCase extends AbstractBaseUseCase<ChatInputCommandInteraction, void> {
  constructor(@inject(TranslationService) private readonly translate: TranslationService) {
    super();
  }

  public async execute(input: ChatInputCommandInteraction): Promise<void> {
    await input.reply({
      content: this.translate.t("CMD_PING_RESPONSE", { ping: input.client.ws.ping }),
      flags: MessageFlags.Ephemeral
    });
  }
}
