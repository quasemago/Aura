import * as Types from "@/infrastructure/config/types";
import { RedisService } from "@/infrastructure/cache/redis-service";
import Axios, { AxiosRequestConfig } from "axios";
import { Inject, Service } from "typedi";
import { Logger } from "winston";
import { Anime, AnimeSearchResponseDTO } from "./dtos/anime-search-response-dto";
import { MalSearchResponseDTO, UserProfile } from "./dtos/mal-search-response-dto";

@Service({ transient: true })
export class JikanService {
  private readonly JIKAN_API_URL = "https://api.jikan.moe/v4";

  constructor(
    @Inject(Types.Logger) private readonly logger: Logger,
    private readonly redisService: RedisService
  ) {}

  public async getAnimeDetails(title: string, sfw: boolean): Promise<Anime | undefined> {
    this.logger.debug(`Fetching anime details for title: ${title}, sfw: ${sfw}`);

    // Check if the anime data is cached.
    const cacheKey = `mal|anime:${title}`;
    const cachedData = await this.redisService.get(cacheKey);
    if (cachedData) {
      this.logger.debug(`Cache hit for title: ${title}`);
      return JSON.parse(cachedData) as Anime;
    }

    try {
      const url = `${this.JIKAN_API_URL}/anime?q=${encodeURIComponent(title)}&limit=1&sfw=${sfw}`;
      const request = this.buildAxiosRequest(url, {
        "User-Agent": "AuraDiscordBot/1.0"
      });

      const response = await Axios.request<AnimeSearchResponseDTO>(request);
      const animeData = response.data;

      this.logger.debug(`Fetched anime details for anime: ${title}`);

      if (!animeData.data || animeData.data.length === 0) {
        return undefined;
      }

      // Save the fetched anime data to cache.
      await this.redisService.set(cacheKey, JSON.stringify(animeData.data[0]));

      return animeData.data[0];
    } catch (err: unknown) {
      const error = err as Error;
      this.logger.error(`Error fetching anime details: ${error.message}`);
      throw err;
    }
  }

  public async getUserProfileDetails(username: string): Promise<UserProfile | undefined> {
    this.logger.debug(`Fetching user profile details for username: ${username}`);

    // Check if the user profile data is cached.
    const cacheKey = `mal|user:${username}`;
    const cachedData = await this.redisService.get(cacheKey);
    if (cachedData) {
      this.logger.debug(`Cache hit for username: ${username}`);
      return JSON.parse(cachedData) as UserProfile;
    }

    try {
      const url = `${this.JIKAN_API_URL}/users/${encodeURIComponent(username)}/full`;
      const request = this.buildAxiosRequest(url, {
        "User-Agent": "AuraDiscordBot/1.0"
      });

      const response = await Axios.request<MalSearchResponseDTO>(request);
      const userData = response.data;

      this.logger.debug(`Fetched profile details for user: ${username}`);

      if (!userData.data) {
        return undefined;
      }

      // Save the fetched user profile data to cache.
      await this.redisService.set(cacheKey, JSON.stringify(userData.data));

      return userData.data;
    } catch (err: unknown) {
      const error = err as Error;
      this.logger.error(`Error fetching anime details: ${error.message}`);
      throw err;
    }
  }

  private buildAxiosRequest(url: string, additionalHeaders?: object): AxiosRequestConfig {
    return {
      headers: {
        ...additionalHeaders
      },
      method: "GET",
      url
      //proxy: false,
    } as AxiosRequestConfig;
  }
}
