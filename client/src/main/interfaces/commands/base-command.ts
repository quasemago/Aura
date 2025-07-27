import { DiscordGuildCommandCategory } from "@/main/interfaces/types/i-command";
import { SlashCommandBuilder } from "discord.js";
import { injectable } from "inversify";

@injectable()
export class BaseDiscordCommandBuilder extends SlashCommandBuilder {
  public category?: DiscordGuildCommandCategory;

  constructor() {
    super();
    this.category = DiscordGuildCommandCategory.NONE;
  }

  public setCategory(category: DiscordGuildCommandCategory): this {
    this.category = category;
    return this;
  }
}
