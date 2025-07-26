export interface IRedisService {
  get: (key: string) => Promise<string | null>;
  set: (key: string, value: string) => Promise<void>;
}
