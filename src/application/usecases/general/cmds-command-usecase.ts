import { DiscordClient } from "@/infrastructure/discord/client";
import { TranslationService } from "@/infrastructure/i18n/translation-service";
import {
  DiscordGuildCommandCategory,
  IDiscordGuildCommand
} from "@/interfaces/discord/types/i-command";
import {
  ChatInputCommandInteraction,
  CommandInteraction,
  EmbedBuilder,
  MessageFlags
} from "discord.js";
import { inject, injectable } from "inversify";
import { validateUserPermissions } from "../../utils";
import { AbstractBaseUseCase } from "../base-usecase";

@injectable()
export class CmdsCommandUseCase extends AbstractBaseUseCase<ChatInputCommandInteraction, void> {
  constructor(@inject(TranslationService) private readonly translate: TranslationService) {
    super();
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    const commandList = this.getCommandListByCategory(interaction);
    if (commandList === undefined) {
      await interaction.reply({
        content: this.translate.t("CMD_CMDS_NO_COMMANDS"),
        flags: MessageFlags.Ephemeral
      });
      return;
    }

    const embed = new EmbedBuilder()
      .setTitle(
        `🤖 ${this.translate.t("CMD_CMDS_TITLE", { botName: interaction.client.user.displayName })}`
      )
      .setDescription(this.translate.t("CMD_CMDS_DESCRIPTION_TEXT"))
      .setColor(0x0099ff)
      .setThumbnail(interaction.client.user.displayAvatarURL())
      .setTimestamp()
      .setFooter({
        text: this.translate.t("COMMON_REQUESTED_BY", { username: interaction.user.username }),
        iconURL: interaction.user.displayAvatarURL()
      });

    for (const [category, commands] of Object.entries(commandList)) {
      const commandNames = commands
        .map((cmd) => `- \`\`/${cmd.data.name}\`\` - ${cmd.data.description}`)
        .join("\n");

      embed.addFields({
        name: this.translateCategory(category),
        value: commandNames || this.translate.t("CMD_CMDS_NO_COMMANDS_IN_CATEGORY"),
        inline: false
      });
    }

    await interaction.reply({
      embeds: [embed],
      flags: MessageFlags.Ephemeral
    });
  }

  private translateCategory(category: string): string {
    const categoryKeyByName: Record<string, string> = {
      [DiscordGuildCommandCategory.NONE]: "CMD_CATEGORY_NONE",
      [DiscordGuildCommandCategory.GENERAL]: "CMD_CATEGORY_GENERAL",
      [DiscordGuildCommandCategory.SEARCHES]: "CMD_CATEGORY_SEARCHES"
    };

    return this.translate.t(categoryKeyByName[category] ?? category);
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
