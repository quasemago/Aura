import { DiscordClient } from "@/infrastructure/discord/client";
import { IDiscordGuildCommand } from "@/interfaces/discord/types/i-command";
import {
  ChatInputCommandInteraction,
  CommandInteraction,
  EmbedBuilder,
  MessageFlags
} from "discord.js";
import { Service } from "typedi";
import { validateUserPermissions } from "../../utils";
import { AbstractBaseUseCase } from "../base-usecase";

@Service({ transient: true })
export class CmdsCommandUseCase extends AbstractBaseUseCase<ChatInputCommandInteraction, void> {
  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    const commandList = this.getCommandListByCategory(interaction);
    if (commandList === undefined) {
      await interaction.reply({
        content: "No commands are currently available.",
        flags: MessageFlags.Ephemeral
      });
      return;
    }

    const embed = new EmbedBuilder()
      .setTitle(`🤖 ${interaction.client.user.displayName} - Command List`)
      .setDescription(
        "Aura is a Discord bot that provides a variety of features to enhance your server."
      )
      .setColor(0x0099ff)
      .setThumbnail(interaction.client.user.displayAvatarURL())
      .setTimestamp()
      .setFooter({
        text: `Requested by ${interaction.user.username}`,
        iconURL: interaction.user.displayAvatarURL()
      });

    for (const [category, commands] of Object.entries(commandList)) {
      const categoryName = category.charAt(0).toUpperCase() + category.slice(1);
      const commandNames = commands
        .map((cmd) => `- \`\`/${cmd.data.name}\`\` - ${cmd.data.description}`)
        .join("\n");

      embed.addFields({
        name: categoryName,
        value: commandNames || "No commands available in this category.",
        inline: false
      });
    }

    await interaction.reply({
      embeds: [embed],
      flags: MessageFlags.Ephemeral
    });
  }

  private getCommandListByCategory(
    interaction: CommandInteraction
  ): Record<string, IDiscordGuildCommand[]> | undefined {
    const discordCommands = (interaction.client as DiscordClient).getCommands();
    if (discordCommands.length === 0) {
      return undefined;
    }
    return discordCommands.reduce<Record<string, IDiscordGuildCommand[]>>((acc, command) => {
      // Check if the user has permission to execute the command.
      if (!validateUserPermissions(interaction, command)) {
        return acc;
      }

      if (!command.metadata.hidden) {
        if (!(command.metadata.category in acc)) {
          acc[command.metadata.category] = [];
        }
        acc[command.metadata.category].push(command);
      }
      return acc;
    }, {});
  }
}
