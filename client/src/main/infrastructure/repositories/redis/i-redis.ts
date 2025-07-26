export interface IRedisRepository {
  get: (key: string) => Promise<string | null>;
  set: (key: string, value: string, durationInSeconds: number) => Promise<void>;
}
