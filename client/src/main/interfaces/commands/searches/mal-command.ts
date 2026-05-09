import { MalCommandUseCase } from "@/main/application/usecases/searches/mal-command-usecase";
import { DiscordGuildCommandCategory } from "@/main/interfaces/types/i-command";
import { InteractionContextType, type ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseCommand } from "../base-command";

@injectable()
export class MalCommand extends BaseCommand {
  constructor(@inject(MalCommandUseCase) private readonly malCommandUseCase: MalCommandUseCase) {
    super({
      name: "mal",
      description: "Search for a user on MyAnimeList",
      category: DiscordGuildCommandCategory.SEARCHES,
      configure: (builder) => {
        builder
          .setContexts(InteractionContextType.Guild)
          .addStringOption((option) =>
            option
              .setName("name")
              .setDescription("The name of the user to search for")
              .setRequired(true)
          );
      }
    });
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.malCommandUseCase.execute(interaction);
  }
}
