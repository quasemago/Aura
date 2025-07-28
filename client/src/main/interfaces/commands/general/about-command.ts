import { AboutCommandUseCase } from "@/main/application/usecases/general/about-command-usecase";
import {
  DiscordGuildCommandCategory,
  type IDiscordGuildCommand
} from "@/main/interfaces/types/i-command";
import { ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseDiscordSlashCommandBuilder } from "../base-command";

@injectable()
export class AboutCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordSlashCommandBuilder;

  constructor(
    @inject(AboutCommandUseCase) private readonly aboutCommandUseCase: AboutCommandUseCase
  ) {
    this.data = new BaseDiscordSlashCommandBuilder()
      .setName("about")
      .setDescription("Help command with information about the bot.")
      .setCategory(DiscordGuildCommandCategory.GENERAL);
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.aboutCommandUseCase.execute(interaction);
  }
}
