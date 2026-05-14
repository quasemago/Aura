import * as Types from "@/infrastructure/config/types";
import * as dotenv from "dotenv";
import { inject, injectable } from "inversify";
import { MaybeNil, MaybeUndefined } from "tsdef";
import { Logger } from "winston";

dotenv.config();

@injectable()
export class Settings {
  constructor(@inject(Types.Logger) private readonly logger: Logger) {}

  // TODO: Make the version dynamic.
  public getBotVersion(): string {
    return "1.0.0";
  }

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

  public getRedisRepositoryHost(): string {
    return this.assertAndReturnSetting("REDIS_HOST");
  }

  public getRedisRepositoryDefaultTTL(): number {
    return Number(this.assertAndReturnSetting("REDIS_TTL", "3600"));
  }

  public getBotDefaultLanguage(): string {
    return this.assertAndReturnSetting("BOT_DEFAULT_LANGUAGE", "en");
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
