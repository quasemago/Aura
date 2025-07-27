import { CommandInteraction, MessageFlags } from "discord.js";
import { injectable } from "inversify";
import { AbstractBaseUseCase } from "./base-usecase";

@injectable()
export class PingCommandUseCase extends AbstractBaseUseCase<CommandInteraction, void> {
  public async execute(input: CommandInteraction): Promise<void> {
    await input.reply({
      content: `:ping_pong: Pong! [${input.client.ws.ping} ms]`,
      flags: MessageFlags.Ephemeral
    });
  }
}
