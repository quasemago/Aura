import type {
  DiscordGuildCommandCategory,
  DiscordGuildCommandMetadata,
  IDiscordGuildCommand
} from "@/interfaces/discord/types/i-command";
import type { ChatInputCommandInteraction } from "discord.js";
import { SlashCommandBuilder } from "discord.js";

export interface DiscordCommandOptions {
  name: string;
  description: string;
  category: DiscordGuildCommandCategory;
  hidden?: boolean;
  cooldownSeconds?: number;
  configure?: (builder: SlashCommandBuilder) => void;
}

export abstract class BaseCommand implements IDiscordGuildCommand {
  public readonly data: SlashCommandBuilder;
  public readonly metadata: DiscordGuildCommandMetadata;

  protected constructor(options: DiscordCommandOptions) {
    this.metadata = {
      category: options.category,
      hidden: options.hidden,
      cooldownSeconds: options.cooldownSeconds
    };

    const builder = new SlashCommandBuilder()
      .setName(options.name)
      .setDescription(options.description);

    options.configure?.(builder);

    this.data = builder;
  }

  public abstract execute(interaction: ChatInputCommandInteraction): Promise<void>;
}
