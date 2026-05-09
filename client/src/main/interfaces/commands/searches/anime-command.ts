import { AnimeCommandUseCase } from "@/main/application/usecases/searches/anime-command-usecase";
import { DiscordGuildCommandCategory } from "@/main/interfaces/types/i-command";
import { InteractionContextType, type ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseCommand } from "../base-command";

@injectable()
export class AnimeCommand extends BaseCommand {
  constructor(
    @inject(AnimeCommandUseCase) private readonly animeCommandUseCase: AnimeCommandUseCase
  ) {
    super({
      name: "anime",
      description: "Search for an anime on MyAnimeList",
      category: DiscordGuildCommandCategory.SEARCHES,
      configure: (builder) => {
        builder
          .setContexts(InteractionContextType.Guild)
          .addStringOption((option) =>
            option
              .setName("title")
              .setDescription("The title of the anime to search for")
              .setRequired(true)
          );
      }
    });
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.animeCommandUseCase.execute(interaction);
  }
}
