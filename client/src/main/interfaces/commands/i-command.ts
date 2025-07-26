import type { DiscordClient } from "@/main/infrastructure/discord/client";
import type { CommandInteraction } from "discord.js";
import type { BaseDiscordCommandBuilder } from "./command-base";

export interface IDiscordGuildCommand {
  data: BaseDiscordCommandBuilder;
  execute(interaction: CommandInteraction, client?: DiscordClient): Promise<void>;
}

export enum DiscordGuildCommandCategory {
  NONE = "None",
  GENERAL = "General",
  SEARCHES = "Searches"
}
