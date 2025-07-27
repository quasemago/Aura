import { CmdsCommandUseCase } from "@/main/application/usecases/cmds-command-usecase";
import { type CommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseDiscordCommandBuilder } from "../base-command";
import { DiscordGuildCommandCategory, type IDiscordGuildCommand } from "../i-command";

@injectable()
export class CmdsCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordCommandBuilder;

  constructor(@inject(CmdsCommandUseCase) private readonly cmdsCommandUseCase: CmdsCommandUseCase) {
    this.data = new BaseDiscordCommandBuilder()
      .setName("cmds")
      .setDescription("Lists all available commands.")
      .setCategory(DiscordGuildCommandCategory.GENERAL);
  }

  public async execute(interaction: CommandInteraction): Promise<void> {
    await this.cmdsCommandUseCase.execute(interaction);
  }
}
