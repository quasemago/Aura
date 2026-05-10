import { PingCommandUseCase } from "@/application/usecases/general/ping-command-usecase";
import { DiscordGuildCommandCategory } from "@/interfaces/discord/types/i-command";
import type { ChatInputCommandInteraction } from "discord.js";
import { Service } from "typedi";
import { BaseCommand } from "../base-command";

@Service({ transient: true })
export class PingCommand extends BaseCommand {
  constructor(private readonly pingCommandUseCase: PingCommandUseCase) {
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
