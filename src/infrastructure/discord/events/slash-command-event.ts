import * as Types from "@/infrastructure/config/types";
import { DiscordClient } from "@/infrastructure/discord/client";
import { Events, Interaction } from "discord.js";
import { Inject, Service } from "typedi";
import { Logger } from "winston";
import { IDiscordGuildEvent } from "./i-events";

@Service()
export class SlashCommandEvent implements IDiscordGuildEvent {
  constructor(@Inject(Types.Logger) private readonly logger: Logger) {}

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
        } catch (err: unknown) {
          this.logger.error("Error while executing slash command.", {
            error: err,
            commandName: interaction.commandName,
            guildId: interaction.guildId,
            channelId: interaction.channelId,
            userId: interaction.user.id
          });

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
