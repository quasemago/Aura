import { AboutCommandUseCase } from "@/application/usecases/general/about-command-usecase";
import { DiscordGuildCommandCategory } from "@/interfaces/discord/types/i-command";
import type { ChatInputCommandInteraction } from "discord.js";
import { Service } from "typedi";
import { BaseCommand } from "../base-command";

@Service({ transient: true })
export class AboutCommand extends BaseCommand {
  constructor(private readonly aboutCommandUseCase: AboutCommandUseCase) {
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
