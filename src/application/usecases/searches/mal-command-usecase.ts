import { UserProfile } from "@/infrastructure/jikan/dtos/mal-search-response-dto";
import { TranslationService } from "@/infrastructure/i18n/translation-service";
import { JikanService } from "@/infrastructure/jikan/jikan-service";
import { ChatInputCommandInteraction, EmbedBuilder } from "discord.js";
import { inject, injectable } from "inversify";
import { AbstractBaseUseCase } from "../base-usecase";

@injectable()
export class MalCommandUseCase extends AbstractBaseUseCase<ChatInputCommandInteraction, void> {
  constructor(
    @inject(JikanService) private readonly jikanService: JikanService,
    @inject(TranslationService) private readonly translate: TranslationService
  ) {
    super();
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    const username = interaction.options.getString("name", true);

    const userData = await this.jikanService.getUserProfileDetails(username);
    if (!userData) {
      await interaction.reply({
        content: this.translate.t("CMD_MAL_NOT_FOUND", { name: username })
      });
      return;
    }

    const embed = this.buildUserProfileEmbed(interaction, userData);
    await interaction.reply({
      embeds: [embed]
    });
  }

  private buildUserProfileEmbed(interaction: ChatInputCommandInteraction, userData: UserProfile) {
    const animeStats = userData.statistics?.anime;
    const defaultValue = this.translate.t("COMMON_NOT_AVAILABLE");

    const embed = new EmbedBuilder()
      .setTitle(this.translate.t("CMD_MAL_PROFILE_TITLE", { username: userData.username }))
      .setURL(userData.url || "#")
      .setColor(0x00ff00)
      .setTimestamp()
      .setFooter({
        text: this.translate.t("COMMON_REQUESTED_BY", { username: interaction.user.username }),
        iconURL: interaction.user.displayAvatarURL()
      })
      .addFields(
        {
          name: this.translate.t("CMD_MAL_FIELD_CURRENTLY_WATCHING"),
          value: animeStats?.watching?.toString() || defaultValue,
          inline: true
        },
        {
          name: this.translate.t("CMD_MAL_FIELD_COMPLETED"),
          value: animeStats?.completed?.toString() || defaultValue,
          inline: true
        },
        {
          name: this.translate.t("CMD_MAL_FIELD_ON_HOLD"),
          value: animeStats?.on_hold?.toString() || defaultValue,
          inline: true
        },
        {
          name: this.translate.t("CMD_MAL_FIELD_DROPPED"),
          value: animeStats?.dropped?.toString() || defaultValue,
          inline: true
        },
        {
          name: this.translate.t("CMD_MAL_FIELD_PLAN_TO_WATCH"),
          value: animeStats?.plan_to_watch?.toString() || defaultValue,
          inline: true
        },
        {
          name: this.translate.t("CMD_MAL_FIELD_TOTAL_DAYS"),
          value: `${animeStats?.total_entries || defaultValue} | ${animeStats?.days_watched || defaultValue}`,
          inline: true
        },
        {
          name: this.translate.t("CMD_MAL_FIELD_MEAN_SCORE"),
          value: animeStats?.mean_score?.toString() || defaultValue,
          inline: true
        },
        {
          name: this.translate.t("CMD_MAL_FIELD_LAST_ONLINE"),
          value: userData.last_online,
          inline: true
        }
      );

    if (userData.images?.jpg?.image_url) {
      embed.setThumbnail(userData.images.jpg.image_url);
    }

    return embed;
  }
}
