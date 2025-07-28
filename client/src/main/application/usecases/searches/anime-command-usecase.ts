import { Anime } from "@/main/infrastructure/services/jikan/dtos/anime-search-response-dto";
import { JikanService } from "@/main/infrastructure/services/jikan/jikan-service";
import { ChatInputCommandInteraction, EmbedBuilder } from "discord.js";
import { inject, injectable } from "inversify";
import { AbstractBaseUseCase } from "../base-usecase";

@injectable()
export class AnimeCommandUseCase extends AbstractBaseUseCase<ChatInputCommandInteraction, void> {
  private readonly DEFAULT_VALUE = "N/A";

  constructor(@inject(JikanService) private readonly jikanService: JikanService) {
    super();
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    const animeName = interaction.options.getString("title", true);

    const animeData = await this.jikanService.getAnimeDetails(animeName, true);
    if (!animeData) {
      await interaction.reply({
        content: `No anime was found with the title: ${animeName}`
      });
      return;
    }

    const embed = this.buildAnimeEmbed(interaction, animeData);
    await interaction.reply({
      embeds: [embed]
    });
  }

  private buildAnimeEmbed(interaction: ChatInputCommandInteraction, animeData: Anime) {
    const synopsis =
      animeData.synopsis.length > 450
        ? animeData.synopsis.substring(0, 450) + "..."
        : animeData.synopsis;
    const genres = animeData.genres
      ? animeData.genres.map((genre) => genre.name).join(", ")
      : this.DEFAULT_VALUE;
    const studios = animeData.studios
      ? animeData.studios.map((studio) => studio.name).join(", ")
      : this.DEFAULT_VALUE;

    const embed = new EmbedBuilder()
      .setTitle(animeData.title || this.DEFAULT_VALUE)
      .setURL(animeData.url || "#")
      .setDescription(
        `**English Tittle:** ${animeData.title_english || this.DEFAULT_VALUE}
        \n\n**Synopsis:** ${synopsis}
        \n\n**Trailer:** ${animeData.trailer?.url || this.DEFAULT_VALUE}`
      )
      .setColor(0x00ff00)
      .setTimestamp()
      .setFooter({
        text: `Requested by ${interaction.user.username}`,
        iconURL: interaction.user.displayAvatarURL()
      })
      .addFields(
        {
          name: "Episodes",
          value: animeData.episodes?.toString() || this.DEFAULT_VALUE,
          inline: true
        },
        {
          name: "Type | Status",
          value: `${animeData.type} | ${animeData.status}`,
          inline: true
        },
        {
          name: "Score | Rank",
          value: `${animeData.score || this.DEFAULT_VALUE} | #${animeData.rank || this.DEFAULT_VALUE}`,
          inline: true
        },
        {
          name: "Genre(s)",
          value: genres,
          inline: true
        },
        {
          name: "Aired",
          value: `${animeData.aired?.prop?.from?.year || this.DEFAULT_VALUE} | ${animeData.season}`,
          inline: true
        },
        {
          name: "Studio(s)",
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
