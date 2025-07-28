import * as Types from "@/main/infrastructure/configurations/types";
import { DiscordClient } from "@/main/infrastructure/discord/client";
import { Events, Interaction } from "discord.js";
import { inject, injectable } from "inversify";
import { Logger } from "winston";
import { IDiscordGuildEvent } from "./i-events";

@injectable()
export class SlashCommandEvent implements IDiscordGuildEvent {
  constructor(@inject(Types.Logger) private readonly logger: Logger) {}

  public handle(client: DiscordClient): void {
    client.on(Events.InteractionCreate, (interaction: Interaction) => {
      (async () => {
        if (!interaction.isChatInputCommand()) {
          return;
        }

        const command = client
          .getCommands()
          .find((cmd) => cmd.data.name === interaction.commandName);
        if (!command) {
          this.logger.error(`No command matching ${interaction.commandName} was found.`);
          return;
        }

        try {
          await command.execute(interaction);
        } catch {
          if (interaction.replied || interaction.deferred) {
            await interaction.followUp({
              content:
                "There was an error while executing this command, report this to an administrator!"
            });
          } else {
            await interaction.reply({
              content:
                "There was an error while executing this command, report this to an administrator!"
            });
          }
        }
      })().catch((err: unknown) => {
        const error = err as Error;
        this.logger.error("Unhandled error in interaction handler:", error);
      });
    });
  }
}
