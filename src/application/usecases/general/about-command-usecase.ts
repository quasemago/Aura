import { Settings } from "@/infrastructure/config/settings";
import { TranslationService } from "@/infrastructure/i18n/translation-service";
import { type ChatInputCommandInteraction, EmbedBuilder, MessageFlags } from "discord.js";
import { inject, injectable } from "inversify";
import { AbstractBaseUseCase } from "../base-usecase";

@injectable()
export class AboutCommandUseCase extends AbstractBaseUseCase<ChatInputCommandInteraction, void> {
  constructor(
    @inject(Settings) private readonly settings: Settings,
    @inject(TranslationService) private readonly translate: TranslationService
  ) {
    super();
  }

  public async execute(input: ChatInputCommandInteraction): Promise<void> {
    const embed = new EmbedBuilder()
      .setTitle(`🤖 ${input.client.user.displayName}`)
      .setDescription(this.translate.t("CMD_ABOUT_DESCRIPTION_TEXT"))
      .setColor(0x0099ff)
      .setThumbnail(input.client.user.displayAvatarURL())
      .setTimestamp()
      .addFields(
        {
          name: this.translate.t("CMD_ABOUT_FIELD_CREATED_BY"),
          value: `quasemago [(Github)](https://github.com/quasemago)`,
          inline: true
        },
        {
          name: this.translate.t("CMD_ABOUT_FIELD_VERSION"),
          value: this.settings.getBotVersion(),
          inline: true
        },
        { name: this.translate.t("CMD_ABOUT_FIELD_UPTIME"), value: "uptime", inline: true }
      )
      .setFooter({
        text: this.translate.t("COMMON_REQUESTED_BY", { username: input.user.username }),
        iconURL: input.user.displayAvatarURL()
      });

    await input.reply({
      embeds: [embed],
      flags: MessageFlags.Ephemeral
    });
  }
}
