import "reflect-metadata";

import { container } from "@/infrastructure/config/container";
import * as Types from "@/infrastructure/config/types";
import { DiscordClient } from "@/infrastructure/discord/client";
import type { Logger } from "winston";

const discordClient = container.get(DiscordClient);
const logger: Logger = container.get(Types.Logger);

discordClient
  .start()
  .then(() => {
    logger.info("Bot is running successfully.");
  })
  .catch((err: unknown) => {
    const error = err as Error;
    logger.error("An error occurred while starting the bot.", error);
  });
