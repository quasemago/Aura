import { UserProfile } from "@/main/infrastructure/services/jikan/dtos/mal-search-response-dto";
import { JikanService } from "@/main/infrastructure/services/jikan/jikan-service";
import { ChatInputCommandInteraction, EmbedBuilder } from "discord.js";
import { inject, injectable } from "inversify";
import { AbstractBaseUseCase } from "./base-usecase";

@injectable()
export class MalCommandUseCase extends AbstractBaseUseCase<ChatInputCommandInteraction, void> {
  private readonly DEFAULT_VALUE = "N/A";

  constructor(@inject(JikanService) private readonly jikanService: JikanService) {
    super();
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    const username = interaction.options.getString("name", true);

    const userData = await this.jikanService.getUserProfileDetails(username);
    if (!userData) {
      await interaction.reply({
        content: `No user was found with name: ${username}`
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

    const embed = new EmbedBuilder()
      .setTitle(`MyAnimeList Profile: ${userData.username}`)
      .setURL(userData.url || "#")
      .setColor(0x00ff00)
      .setTimestamp()
      .setFooter({
        text: `Requested by ${interaction.user.username}`,
        iconURL: interaction.user.displayAvatarURL()
      })
      .addFields(
        {
          name: ":green_heart: Currently Watching",
          value: animeStats?.watching?.toString() || this.DEFAULT_VALUE,
          inline: true
        },
        {
          name: ":blue_heart: Completed",
          value: animeStats?.completed?.toString() || this.DEFAULT_VALUE,
          inline: true
        },
        {
          name: ":yellow_heart: On Hold",
          value: animeStats?.on_hold?.toString() || this.DEFAULT_VALUE,
          inline: true
        },
        {
          name: ":broken_heart: Dropped",
          value: animeStats?.dropped?.toString() || this.DEFAULT_VALUE,
          inline: true
        },
        {
          name: ":white_circle: Plan to Watch",
          value: animeStats?.plan_to_watch?.toString() || this.DEFAULT_VALUE,
          inline: true
        },
        {
          name: ":page_facing_up: Total | Days",
          value: `${animeStats?.total_entries || this.DEFAULT_VALUE} | ${animeStats?.days_watched || this.DEFAULT_VALUE}`,
          inline: true
        },
        {
          name: ":bar_chart: Mean Score",
          value: animeStats?.mean_score?.toString() || this.DEFAULT_VALUE,
          inline: true
        },
        {
          name: ":date: Last Online",
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
