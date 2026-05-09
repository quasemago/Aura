import type { DiscordClient } from "@/infrastructure/discord/client";

export interface IDiscordGuildEvent {
  handle: (client: DiscordClient) => void;
}
