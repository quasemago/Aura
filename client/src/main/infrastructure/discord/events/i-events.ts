import type { DiscordClient } from "@/main/infrastructure/discord/client";

export interface IDiscordGuildEvent {
  handle: (client: DiscordClient) => void;
}
