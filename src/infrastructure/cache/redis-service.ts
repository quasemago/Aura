import { Settings } from "@/infrastructure/config/settings";
import * as Types from "@/infrastructure/config/types";
import { RedisRepository } from "@/infrastructure/cache/redis-repository";
import { Inject, Service } from "typedi";
import { Logger } from "winston";

@Service({ transient: true })
export class RedisService {
  constructor(
    @Inject(Types.Logger) private readonly logger: Logger,
    private readonly settings: Settings,
    private readonly redisRepository: RedisRepository
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
