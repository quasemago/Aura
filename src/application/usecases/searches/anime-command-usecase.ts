import { Anime } from "@/infrastructure/jikan/dtos/anime-search-response-dto";
import { TranslationService } from "@/infrastructure/i18n/translation-service";
import { JikanService } from "@/infrastructure/jikan/jikan-service";
import { ChatInputCommandInteraction, EmbedBuilder } from "discord.js";
import { inject, injectable } from "inversify";
import { AbstractBaseUseCase } from "../base-usecase";

@injectable()
export class AnimeCommandUseCase extends AbstractBaseUseCase<ChatInputCommandInteraction, void> {
  constructor(
    @inject(JikanService) private readonly jikanService: JikanService,
    @inject(TranslationService) private readonly translate: TranslationService
  ) {
    super();
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    const animeName = interaction.options.getString("title", true);

    const animeData = await this.jikanService.getAnimeDetails(animeName, true);
    if (!animeData) {
      await interaction.reply({
        content: this.translate.t("CMD_ANIME_NOT_FOUND", { title: animeName })
      });
      return;
    }

    const embed = this.buildAnimeEmbed(interaction, animeData);
    await interaction.reply({
      embeds: [embed]
    });
  }

  private buildAnimeEmbed(interaction: ChatInputCommandInteraction, animeData: Anime) {
    const defaultValue = this.translate.t("COMMON_NOT_AVAILABLE");
    const synopsis =
      animeData.synopsis.length > 450
        ? animeData.synopsis.substring(0, 450) + "..."
        : animeData.synopsis;
    const genres = animeData.genres
      ? animeData.genres.map((genre) => genre.name).join(", ")
      : defaultValue;
    const studios = animeData.studios
      ? animeData.studios.map((studio) => studio.name).join(", ")
      : defaultValue;

    const embed = new EmbedBuilder()
      .setTitle(animeData.title || defaultValue)
      .setURL(animeData.url || "#")
      .setDescription(
        `**${this.translate.t("CMD_ANIME_DESCRIPTION_ENGLISH_TITLE")}:** ${
          animeData.title_english || defaultValue
        }
        \n**${this.translate.t("CMD_ANIME_DESCRIPTION_SYNOPSIS")}:** ${synopsis}
        \n**${this.translate.t("CMD_ANIME_DESCRIPTION_TRAILER")}:** ${
          animeData.trailer?.url || defaultValue
        }`
      )
      .setColor(0x00ff00)
      .setTimestamp()
      .setFooter({
        text: this.translate.t("COMMON_REQUESTED_BY", { username: interaction.user.username }),
        iconURL: interaction.user.displayAvatarURL()
      })
      .addFields(
        {
          name: this.translate.t("CMD_ANIME_FIELD_EPISODES"),
          value: animeData.episodes?.toString() || defaultValue,
          inline: true
        },
        {
          name: this.translate.t("CMD_ANIME_FIELD_TYPE_STATUS"),
          value: `${animeData.type} | ${animeData.status}`,
          inline: true
        },
        {
          name: this.translate.t("CMD_ANIME_FIELD_SCORE_RANK"),
          value: `${animeData.score || defaultValue} | #${animeData.rank || defaultValue}`,
          inline: true
        },
        {
          name: this.translate.t("CMD_ANIME_FIELD_GENRES"),
          value: genres,
          inline: true
        },
        {
          name: this.translate.t("CMD_ANIME_FIELD_AIRED"),
          value: `${animeData.aired?.prop?.from?.year || defaultValue} | ${animeData.season}`,
          inline: true
        },
        {
          name: this.translate.t("CMD_ANIME_FIELD_STUDIOS"),
          value: studios,
          inline: true
        }
      );

    if (animeData.images?.jpg?.image_url) {
      embed.setImage(animeData.images.jpg.image_url);
    }

    return embed;
  }
}
