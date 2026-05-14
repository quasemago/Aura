import * as Types from "@/infrastructure/config/types";
import { DiscordClient } from "@/infrastructure/discord/client";
import { TranslationService } from "@/infrastructure/i18n/translation-service";
import { Events, Interaction } from "discord.js";
import { inject, injectable } from "inversify";
import { Logger } from "winston";
import { IDiscordGuildEvent } from "./i-events";

@injectable()
export class SlashCommandEvent implements IDiscordGuildEvent {
  constructor(
    @inject(Types.Logger) private readonly logger: Logger,
    @inject(TranslationService) private readonly translate: TranslationService
  ) {}

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
              content: this.translate.t("DISCORD_COMMAND_ERROR_EXECUTION")
            });
          } else {
            await interaction.reply({
              content: this.translate.t("DISCORD_COMMAND_ERROR_EXECUTION")
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
