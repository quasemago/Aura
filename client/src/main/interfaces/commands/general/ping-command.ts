import { MessageFlags, type CommandInteraction } from "discord.js";
import { injectable } from "inversify";
import { BaseDiscordCommandBuilder } from "../command-base";
import { DiscordGuildCommandCategory, type IDiscordGuildCommand } from "../i-command";

@injectable()
export class PingCommand implements IDiscordGuildCommand {
  public readonly data: BaseDiscordCommandBuilder;

  constructor() {
    this.data = new BaseDiscordCommandBuilder()
      .setName("ping")
      .setDescription("Replies with Pong!")
      .setCategory(DiscordGuildCommandCategory.GENERAL);
  }

  public async execute(interaction: CommandInteraction): Promise<void> {
    await interaction.reply({
      content: `:ping_pong: Pong! [${interaction.client.ws.ping} ms]`,
      flags: MessageFlags.Ephemeral
    });
  }
}
