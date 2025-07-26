/* eslint-disable sonarjs/no-invariant-returns */
/* eslint-disable sonarjs/prefer-single-boolean-return */
import type { IDiscordGuildCommand } from "@/main/interfaces/commands/i-command";
import { InteractionContextType, type CommandInteraction } from "discord.js";

export const validateUserPermissions = (
  _interaction: CommandInteraction,
  command: IDiscordGuildCommand
): boolean => {
  // If the command context is BotDM or PrivateChannel, we allow it to be executed
  // regardless of the user's permissions.
  if (
    command.data.contexts?.includes(
      InteractionContextType.BotDM | InteractionContextType.PrivateChannel
    )
  ) {
    return true;
  }
  // TODO: Check if the user has the required permissions to execute the command
  // return interaction.memberPermissions?.has(command.data.default_member_permissions, true) ?? false;
  return true;
};
