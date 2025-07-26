import { EmbedBuilder, MessageFlags, type CommandInteraction } from "discord.js";
import { injectable } from "inversify";
import { BaseDiscordCommandBuilder } from "../command-base";
import { DiscordGuildCommandCategory, type IDiscordGuildCommand } from "../i-command";

@injectable()
export class AboutCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordCommandBuilder;

  constructor() {
    this.data = new BaseDiscordCommandBuilder()
      .setName("about")
      .setDescription("Help command with information about the bot.")
      .setCategory(DiscordGuildCommandCategory.GENERAL);
  }

  public async execute(interaction: CommandInteraction): Promise<void> {
    const embed = new EmbedBuilder()
      .setTitle(`🤖 ${interaction.client.user.displayName}`)
      .setDescription(
        "Aura is a Discord bot that provides a variety of features to enhance your server."
      )
      .setColor(0x0099ff)
      .setThumbnail(interaction.client.user.displayAvatarURL())
      .setTimestamp()
      .addFields(
        {
          name: "Created by",
          value: `quasemago [(Github)](https://github.com/quasemago)`,
          inline: true
        },
        { name: "Version", value: "0.0.1", inline: true },
        { name: "Uptime", value: "uptime", inline: true }
      )
      .setFooter({
        text: `Requested by ${interaction.user.username}`,
        iconURL: interaction.user.displayAvatarURL()
      });

    await interaction.reply({
      embeds: [embed],
      flags: MessageFlags.Ephemeral
    });
  }
}
