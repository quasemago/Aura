import { Settings } from "@/infrastructure/config/settings";
import * as Types from "@/infrastructure/config/types";
import { RedisRepository } from "@/infrastructure/cache/redis-repository";
import { inject, injectable } from "inversify";
import { Logger } from "winston";

@injectable()
export class RedisService {
  constructor(
    @inject(Types.Logger) private readonly logger: Logger,
    @inject(Settings) private readonly settings: Settings,
    @inject(RedisRepository) private readonly redisRepository: RedisRepository
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
