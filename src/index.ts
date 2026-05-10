import "reflect-metadata";

import { container } from "@/infrastructure/config/container";
import * as Types from "@/infrastructure/config/types";
import { DiscordClient } from "@/infrastructure/discord/client";
import type { Logger } from "winston";

const discordClient = container.get(DiscordClient);
const logger: Logger = container.get(Types.Logger);

try {
  await discordClient.start();
  logger.info("Bot is running successfully.");
} catch (err: unknown) {
  logger.error("An error occurred while starting the bot.", err as Error);
}
