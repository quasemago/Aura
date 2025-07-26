import { container } from "@/main/infrastructure/configurations/container";
import * as Types from "@/main/infrastructure/configurations/types";
import { DiscordClient } from "@/main/infrastructure/discord/client";
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
