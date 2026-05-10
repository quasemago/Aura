import type { IDiscordGuildEvent } from "@/infrastructure/discord/events/i-events";
import type { IDiscordGuildCommand } from "@/interfaces/discord/types/i-command";
import { Token } from "typedi";
import type { Logger as WinstonLogger } from "winston";

// Core
export const Logger = new Token<WinstonLogger>("Logger");

// Interfaces
export const DiscordGuildEvent = new Token<IDiscordGuildEvent[]>("DiscordGuildEvent");
export const DiscordGuildCommand = new Token<IDiscordGuildCommand[]>("DiscordGuildCommand");
