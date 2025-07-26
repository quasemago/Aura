import { Settings } from "@/main/infrastructure/configurations/settings";
import * as Types from "@/main/infrastructure/configurations/types";
import { IRedisRepository } from "@/main/infrastructure/repositories/redis/i-redis";
import { RedisRepository } from "@/main/infrastructure/repositories/redis/redis-repository";
import { inject, injectable } from "inversify";
import { Logger } from "winston";
import type { IRedisService } from "./i-redis-service";

@injectable()
export class RedisService implements IRedisService {
  constructor(
    @inject(Types.Logger) private readonly logger: Logger,
    @inject(Settings) private readonly settings: Settings,
    @inject(RedisRepository) private readonly redisRepository: IRedisRepository
  ) {}

  public async get(key: string): Promise<string | null> {
    this.logger.debug(`Fetching value for key: ${key}`);
    return await this.redisRepository.get(key);
  }

  public async set(key: string, value: string): Promise<void> {
    this.logger.debug(`Setting value for key: ${key} with value: ${value}`);
    await this.redisRepository.set(key, value, this.settings.getRedisRepositoryDefaultTTL());
  }
}
