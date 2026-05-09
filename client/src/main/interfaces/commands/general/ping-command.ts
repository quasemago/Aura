import { PingCommandUseCase } from "@/main/application/usecases/general/ping-command-usecase";
import { DiscordGuildCommandCategory } from "@/main/interfaces/types/i-command";
import type { ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseCommand } from "../base-command";

@injectable()
export class PingCommand extends BaseCommand {
  constructor(@inject(PingCommandUseCase) private readonly pingCommandUseCase: PingCommandUseCase) {
    super({
      name: "ping",
      description: "Replies with Pong!",
      category: DiscordGuildCommandCategory.GENERAL
    });
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.pingCommandUseCase.execute(interaction);
  }
}
