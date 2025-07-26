import { validateUserPermissions } from "@/main/application/utils";
import { DiscordClient } from "@/main/infrastructure/discord/client";
import { EmbedBuilder, MessageFlags, type CommandInteraction } from "discord.js";
import { injectable } from "inversify";
import { BaseDiscordCommandBuilder } from "../command-base";
import { DiscordGuildCommandCategory, type IDiscordGuildCommand } from "../i-command";

@injectable()
export class CmdsCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordCommandBuilder;

  constructor() {
    this.data = new BaseDiscordCommandBuilder()
      .setName("cmds")
      .setDescription("Lists all available commands.")
      .setCategory(DiscordGuildCommandCategory.GENERAL);
  }

  public async execute(interaction: CommandInteraction, client: DiscordClient): Promise<void> {
    const commandList = this.getCommandListByCategory(interaction, client);
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
    interaction: CommandInteraction,
    client: DiscordClient
  ): Record<string, IDiscordGuildCommand[]> | undefined {
    const commands = client.getCommands();
    if (commands.length === 0) {
      return undefined;
    }
    return commands.reduce<Record<string, IDiscordGuildCommand[]>>((acc, command) => {
      // Check if the user has permission to execute the command.
      if (!validateUserPermissions(interaction, command)) {
        return acc;
      }

      if (command.data.category !== undefined) {
        if (!(command.data.category in acc)) {
          acc[command.data.category] = [];
        }
        acc[command.data.category].push(command);
      }
      return acc;
    }, {});
  }
}
