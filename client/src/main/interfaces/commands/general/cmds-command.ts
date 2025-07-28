import { CmdsCommandUseCase } from "@/main/application/usecases/cmds-command-usecase";
import {
  DiscordGuildCommandCategory,
  type IDiscordGuildCommand
} from "@/main/interfaces/types/i-command";
import { type ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseDiscordSlashCommandBuilder } from "../base-command";

@injectable()
export class CmdsCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordSlashCommandBuilder;

  constructor(@inject(CmdsCommandUseCase) private readonly cmdsCommandUseCase: CmdsCommandUseCase) {
    this.data = new BaseDiscordSlashCommandBuilder()
      .setName("cmds")
      .setDescription("Lists all available commands.")
      .setCategory(DiscordGuildCommandCategory.GENERAL);
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.cmdsCommandUseCase.execute(interaction);
  }
}
