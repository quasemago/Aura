import type { CommandInteraction } from "discord.js";
import type { BaseDiscordCommandBuilder } from "./base-command";

export interface IDiscordGuildCommand {
  data: BaseDiscordCommandBuilder;
  execute(interaction: CommandInteraction): Promise<void>;
}

export enum DiscordGuildCommandCategory {
  NONE = "None",
  GENERAL = "General",
  SEARCHES = "Searches"
}
