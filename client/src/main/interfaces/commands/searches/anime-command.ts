import { AnimeCommandUseCase } from "@/main/application/usecases/anime-command-usecase";
import {
  DiscordGuildCommandCategory,
  type IDiscordGuildCommand
} from "@/main/interfaces/types/i-command";
import { InteractionContextType, type ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseDiscordSlashCommandBuilder } from "../base-command";

@injectable()
export class AnimeCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordSlashCommandBuilder;

  constructor(
    @inject(AnimeCommandUseCase) private readonly animeCommandUseCase: AnimeCommandUseCase
  ) {
    const builder = new BaseDiscordSlashCommandBuilder()
      .setName("anime")
      .setDescription("Search for an anime on MyAnimeList")
      .setCategory(DiscordGuildCommandCategory.SEARCHES)
      .setContexts(InteractionContextType.Guild);

    builder.addStringOption((option) =>
      option
        .setName("title")
        .setDescription("The title of the anime to search for")
        .setRequired(true)
    );

    this.data = builder;
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.animeCommandUseCase.execute(interaction);
  }
}
