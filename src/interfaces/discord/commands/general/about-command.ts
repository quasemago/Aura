import { AboutCommandUseCase } from "@/application/usecases/general/about-command-usecase";
import { DiscordGuildCommandCategory } from "@/interfaces/discord/types/i-command";
import type { ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseCommand } from "../base-command";

@injectable()
export class AboutCommand extends BaseCommand {
  constructor(
    @inject(AboutCommandUseCase) private readonly aboutCommandUseCase: AboutCommandUseCase
  ) {
    super({
      name: "about",
      description: "Help command with information about the bot.",
      category: DiscordGuildCommandCategory.GENERAL
    });
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.aboutCommandUseCase.execute(interaction);
  }
}
