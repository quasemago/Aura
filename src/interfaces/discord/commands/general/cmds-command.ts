import { CmdsCommandUseCase } from "@/application/usecases/general/cmds-command-usecase";
import { DiscordGuildCommandCategory } from "@/interfaces/discord/types/i-command";
import { type ChatInputCommandInteraction } from "discord.js";
import { Service } from "typedi";
import { BaseCommand } from "../base-command";

@Service({ transient: true })
export class CmdsCommand extends BaseCommand {
  constructor(private readonly cmdsCommandUseCase: CmdsCommandUseCase) {
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
