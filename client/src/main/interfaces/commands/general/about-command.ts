import { AboutCommandUseCase } from "@/main/application/usecases/about-command-usecase";
import {
  DiscordGuildCommandCategory,
  type IDiscordGuildCommand
} from "@/main/interfaces/types/i-command";
import { PermissionFlagsBits, type CommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseDiscordCommandBuilder } from "../base-command";

@injectable()
export class AboutCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordCommandBuilder;

  constructor(
    @inject(AboutCommandUseCase) private readonly aboutCommandUseCase: AboutCommandUseCase
  ) {
    this.data = new BaseDiscordCommandBuilder()
      .setName("about")
      .setDescription("Help command with information about the bot.")
      .setCategory(DiscordGuildCommandCategory.GENERAL)
      .setDefaultMemberPermissions(PermissionFlagsBits.Administrator);
  }

  public async execute(interaction: CommandInteraction): Promise<void> {
    await this.aboutCommandUseCase.execute(interaction);
  }
}
