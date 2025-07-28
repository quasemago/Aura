import { MalCommandUseCase } from "@/main/application/usecases/searches/mal-command-usecase";
import {
  DiscordGuildCommandCategory,
  type IDiscordGuildCommand
} from "@/main/interfaces/types/i-command";
import { InteractionContextType, type ChatInputCommandInteraction } from "discord.js";
import { inject, injectable } from "inversify";
import { BaseDiscordSlashCommandBuilder } from "../base-command";

@injectable()
export class MalCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordSlashCommandBuilder;

  constructor(@inject(MalCommandUseCase) private readonly malCommandUseCase: MalCommandUseCase) {
    const builder = new BaseDiscordSlashCommandBuilder()
      .setName("mal")
      .setDescription("Search for a user on MyAnimeList")
      .setCategory(DiscordGuildCommandCategory.SEARCHES)
      .setContexts(InteractionContextType.Guild);

    builder.addStringOption((option) =>
      option.setName("name").setDescription("The name of the user to search for").setRequired(true)
    );

    this.data = builder;
  }

  public async execute(interaction: ChatInputCommandInteraction): Promise<void> {
    await this.malCommandUseCase.execute(interaction);
  }
}
