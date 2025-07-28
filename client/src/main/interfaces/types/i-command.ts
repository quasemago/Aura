import type { BaseDiscordSlashCommandBuilder } from "@/main/interfaces/commands/base-command";
import type { ChatInputCommandInteraction } from "discord.js";

export interface IDiscordGuildCommand {
  data: BaseDiscordSlashCommandBuilder;
  execute(interaction: ChatInputCommandInteraction): Promise<void>;
}

export enum DiscordGuildCommandCategory {
  NONE = "None",
  GENERAL = "General",
  SEARCHES = "Searches"
}
