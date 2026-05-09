import { CmdsCommandUseCase } from "@/main/application/usecases/general/cmds-command-usecase";
import { DiscordGuildCommandCategory } from "@/main/interfaces/types/i-command";
import { type ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseCommand } from "../base-command";

@injectable()
export class CmdsCommand extends BaseCommand {
  constructor(@inject(CmdsCommandUseCase) private readonly cmdsCommandUseCase: CmdsCommandUseCase) {
    super({
      name: "cmds",
      description: "Lists all available commands.",
      category: DiscordGuildCommandCategory.GENERAL
    });
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.cmdsCommandUseCase.execute(interaction);
  }
}
