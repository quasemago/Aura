import type { BaseDiscordCommandBuilder } from "@/main/interfaces/commands/base-command";
import type { CommandInteraction } from "discord.js";

export interface IDiscordGuildCommand {
  data: BaseDiscordCommandBuilder;
  execute(interaction: CommandInteraction): Promise<void>;
}

export enum DiscordGuildCommandCategory {
  NONE = "None",
  GENERAL = "General",
  SEARCHES = "Searches"
}
