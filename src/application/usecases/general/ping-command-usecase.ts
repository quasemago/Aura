import { type ChatInputCommandInteraction, MessageFlags } from "discord.js";
import { Service } from "typedi";
import { AbstractBaseUseCase } from "../base-usecase";

@Service({ transient: true })
export class PingCommandUseCase extends AbstractBaseUseCase<ChatInputCommandInteraction, void> {
  public async execute(input: ChatInputCommandInteraction): Promise<void> {
    await input.reply({
      content: `:ping_pong: Pong! [${input.client.ws.ping} ms]`,
      flags: MessageFlags.Ephemeral
    });
  }
}
