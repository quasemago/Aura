import * as Types from "@/main/infrastructure/configurations/types";
import * as dotenv from "dotenv";
import { inject, injectable } from "inversify";
import { MaybeNil, MaybeUndefined } from "tsdef";
import { Logger } from "winston";

dotenv.config();

@injectable()
export class Settings {
  constructor(@inject(Types.Logger) private readonly logger: Logger) {}

  public getDiscordToken(): string {
    return this.assertAndReturnSetting("BOT_TOKEN");
  }

  public getDiscordClientId(): string {
    return this.assertAndReturnSetting("BOT_ID");
  }

  public getBotPresenceMessage(): string {
    return this.assertAndReturnSetting("BOT_PRESENCE_MSG", "Discord");
  }

  public getBotPresenceType(): number {
    return Number(this.assertAndReturnSetting("BOT_PRESENCE_TYPE", "0"));
  }

  private assertAndReturnSetting(settingName: string, defaultValue?: string): string {
    const setting: MaybeUndefined<string> = this.returnSetting(settingName);
    if (setting === undefined) {
      if (defaultValue !== undefined) {
        this.logger.warn(`Using default value for ${settingName}: ${defaultValue}`);
        return defaultValue;
      }
      const message = `You need to configure the environment variable ${settingName}`;
      this.logger.error(message);
      throw new Error(message);
    }
    return setting;
  }

  private returnSetting(settingName: string): MaybeUndefined<string> {
    const setting: MaybeNil<string> = process.env[settingName];
    if (setting === "null") {
      return undefined;
    }
    return setting;
  }
}
