import type { ChatInputCommandInteraction, SlashCommandBuilder } from "discord.js";

export interface DiscordGuildCommandMetadata {
  category: DiscordGuildCommandCategory;
  hidden?: boolean;
  cooldownSeconds?: number;
}

export interface IDiscordGuildCommand {
  data: SlashCommandBuilder;
  metadata: DiscordGuildCommandMetadata;
  execute(interaction: ChatInputCommandInteraction): Promise<void>;
}

export enum DiscordGuildCommandCategory {
  NONE = "None",
  GENERAL = "General",
  SEARCHES = "Searches"
}
