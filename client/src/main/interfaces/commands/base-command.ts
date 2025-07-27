import { SlashCommandBuilder } from "discord.js";
import { DiscordGuildCommandCategory } from "./i-command";
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
