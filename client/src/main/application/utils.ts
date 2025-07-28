import type { IDiscordGuildCommand } from "@/main/interfaces/types/i-command";
import { InteractionContextType, type CommandInteraction } from "discord.js";

export const validateUserPermissions = (
  interaction: CommandInteraction,
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

  // If the command does not have any specific permissions set, we allow it to be executed.
  const commandPermissions = command.data.default_member_permissions;
  if (commandPermissions == null) {
    return true;
  }

  // If the user does not have the required permissions, we deny execution.
  return interaction.memberPermissions?.has(BigInt(commandPermissions), true) ?? false;
};
