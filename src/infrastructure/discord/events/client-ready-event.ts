import { Settings } from "@/infrastructure/config/settings";
import * as Types from "@/infrastructure/config/types";
import { DiscordClient } from "@/infrastructure/discord/client";
import { Events } from "discord.js";
import { inject, injectable } from "inversify";
import { Logger } from "winston";
import { IDiscordGuildEvent } from "./i-events";

@injectable()
export class ClientReadyEvent implements IDiscordGuildEvent {
  constructor(
    @inject(Types.Logger) private readonly logger: Logger,
    @inject(Settings) private readonly settings: Settings
  ) {}

  public handle(client: DiscordClient): void {
    client.once(Events.ClientReady, () => {
      try {
        if (client.isReady()) {
          this.logger.info(`Logged in as ${client.user.tag}!`);
          client.user.setPresence({
            activities: [
              {
                type: this.settings.getBotPresenceType(),
                name: this.settings.getBotPresenceMessage()
              }
            ],
            status: "online"
          });
        }
      } catch (err: unknown) {
        this.logger.error("Unhandled error in event handler:", err as Error);
      }
    });
  }
}
