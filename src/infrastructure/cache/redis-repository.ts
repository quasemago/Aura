import { Settings } from "@/infrastructure/config/settings";
import * as Types from "@/infrastructure/config/types";
import { Redis } from "ioredis";
import { Inject, Service } from "typedi";
import { Logger } from "winston";

@Service()
export class RedisRepository {
  constructor(
    @Inject(Types.Logger) private readonly logger: Logger,
    private readonly settings: Settings
  ) {}

  public async get(key: string): Promise<string | null> {
    let client: Redis | null = null;
    try {
      client = await this.createRedisClient();
      return await client.get(key);
    } catch (err: unknown) {
      const error = err as Error;
      this.logger.error(`Error getting key ${key} from Redis:`, error);
    } finally {
      if (client) {
        await this.closeConnection(client);
      }
    }
    return null;
  }

  public async set(key: string, value: string, durationInSeconds: number): Promise<void> {
    let client: Redis | null = null;
    try {
      client = await this.createRedisClient();
      await client.set(key, value, "EX", durationInSeconds);
    } catch (err: unknown) {
      const error = err as Error;
      this.logger.error(`Error setting key ${key} in Redis:`, error);
    } finally {
      if (client) {
        await this.closeConnection(client);
      }
    }
  }

  private async createRedisClient(): Promise<Redis> {
    try {
      const client = new Redis({
        host: this.settings.getRedisRepositoryHost(),
        port: 6379
        // TODO: configure timeout settings
      });

      client.on("error", (err: Error) => {
        this.logger.error("Redis error:", err);
        this.closeConnection(client).catch(() => {});
      });

      return client;
    } catch (err: unknown) {
      const error = err as Error;
      this.logger.error("Error creating Redis client:", error);
      throw error;
    }
  }

  private async closeConnection(client: Redis): Promise<void> {
    if (client.status === "ready") {
      try {
        await client.quit();
      } catch (err: unknown) {
        const error = err as Error;
        this.logger.error("Error closing Redis connection:", error);
      }
    }
  }
}
