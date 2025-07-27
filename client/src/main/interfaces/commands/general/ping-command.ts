import { CmdsCommandUseCase } from "@/main/application/usecases/cmds-command-usecase";
import type { CommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseDiscordCommandBuilder } from "../base-command";
import { DiscordGuildCommandCategory, type IDiscordGuildCommand } from "../i-command";

@injectable()
export class PingCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordCommandBuilder;

  constructor(@inject(CmdsCommandUseCase) private readonly pingCommandUseCase: CmdsCommandUseCase) {
    this.data = new BaseDiscordCommandBuilder()
      .setName("ping")
      .setDescription("Replies with Pong!")
      .setCategory(DiscordGuildCommandCategory.GENERAL);
  }

  public async execute(interaction: CommandInteraction): Promise<void> {
    await this.pingCommandUseCase.execute(interaction);
  }
}
