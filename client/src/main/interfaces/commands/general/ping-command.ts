import { PingCommandUseCase } from "@/main/application/usecases/general/ping-command-usecase";
import {
  DiscordGuildCommandCategory,
  type IDiscordGuildCommand
} from "@/main/interfaces/types/i-command";
import type { ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseDiscordSlashCommandBuilder } from "../base-command";

@injectable()
export class PingCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordSlashCommandBuilder;

  constructor(@inject(PingCommandUseCase) private readonly pingCommandUseCase: PingCommandUseCase) {
    this.data = new BaseDiscordSlashCommandBuilder()
      .setName("ping")
      .setDescription("Replies with Pong!")
      .setCategory(DiscordGuildCommandCategory.GENERAL);
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.pingCommandUseCase.execute(interaction);
  }
}
