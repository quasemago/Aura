import type { DiscordGuildCommandCategory } from "@/main/interfaces/types/i-command";
import { SlashCommandBuilder } from "discord.js";

export interface SlashCommandCategory {
  category?: DiscordGuildCommandCategory;
}

export class BaseDiscordSlashCommandBuilder
  extends SlashCommandBuilder
  implements SlashCommandCategory
{
  public category?: DiscordGuildCommandCategory;

  public setCategory(category: DiscordGuildCommandCategory): this {
    this.category = category;
    return this;
  }
}
