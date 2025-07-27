import { PingCommandUseCase } from "@/main/application/usecases/ping-command-usecase";
import {
  DiscordGuildCommandCategory,
  type IDiscordGuildCommand
} from "@/main/interfaces/types/i-command";
import type { CommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseDiscordCommandBuilder } from "../base-command";

@injectable()
export class PingCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordCommandBuilder;

  constructor(@inject(PingCommandUseCase) private readonly pingCommandUseCase: PingCommandUseCase) {
    this.data = new BaseDiscordCommandBuilder()
      .setName("ping")
      .setDescription("Replies with Pong!")
      .setCategory(DiscordGuildCommandCategory.GENERAL);
  }

  public async execute(interaction: CommandInteraction): Promise<void> {
    await this.pingCommandUseCase.execute(interaction);
  }
}
