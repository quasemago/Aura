import { Settings } from "@/infrastructure/config/settings";
import * as Types from "@/infrastructure/config/types";
import { Redis } from "ioredis";
import { Inject, Service } from "typedi";
import { Logger } from "winston";

@Service()
export class RedisRepository {
  private client: Redis | undefined;

  constructor(
    @Inject(Types.Logger) private readonly logger: Logger,
    private readonly settings: Settings
  ) {}

  public async get(key: string, del?: boolean): Promise<string | null> {
    try {
      const client = this.getRedisInstance();
      return del ? await client.getdel(key) : await client.get(key);
    } catch (err: unknown) {
      this.logger.error(`Error getting key ${key} from Redis:`, err as Error);
    }
    return null;
  }

  public async set(key: string, value: string, durationInSeconds: number): Promise<void> {
    try {
      const client = this.getRedisInstance();
      await client.set(key, value, "EX", durationInSeconds);
    } catch (err: unknown) {
      this.logger.error(`Error setting key ${key} in Redis:`, err as Error);
    }
  }

  private getRedisInstance(): Redis {
    if (this.client && this.client.status !== "end") {
      return this.client;
    }

    this.client = this.createRedisInstance();
    return this.client;
  }

  private createRedisInstance(): Redis {
    try {
      const client = new Redis({
        host: this.settings.getRedisRepositoryHost(),
        port: 6379
        // TODO: configure timeout settings
      });

      client.on("error", (err: Error) => {
        this.logger.error("Redis error:", err);
      });

      client.on("end", () => {
        if (this.client === client) {
          this.client = undefined;
        }
      });

      return client;
    } catch (err: unknown) {
      const error = err as Error;
      this.logger.error("Error creating Redis client:", error);
      throw error;
    }
  }
}
