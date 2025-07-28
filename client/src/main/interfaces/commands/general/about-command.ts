import { AboutCommandUseCase } from "@/main/application/usecases/about-command-usecase";
import {
  DiscordGuildCommandCategory,
  type IDiscordGuildCommand
} from "@/main/interfaces/types/i-command";
import { ChatInputCommandInteraction, PermissionFlagsBits } from "discord.js";
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
      .setCategory(DiscordGuildCommandCategory.GENERAL)
      .setDefaultMemberPermissions(PermissionFlagsBits.Administrator);
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.aboutCommandUseCase.execute(interaction);
  }
}
