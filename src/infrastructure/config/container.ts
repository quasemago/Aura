import type { AbstractBaseUseCase } from "@/application/usecases/base-usecase";
import { AboutCommandUseCase } from "@/application/usecases/general/about-command-usecase";
import { CmdsCommandUseCase } from "@/application/usecases/general/cmds-command-usecase";
import { PingCommandUseCase } from "@/application/usecases/general/ping-command-usecase";
import { AnimeCommandUseCase } from "@/application/usecases/searches/anime-command-usecase";
import { MalCommandUseCase } from "@/application/usecases/searches/mal-command-usecase";
import { Settings } from "@/infrastructure/config/settings";
import * as Types from "@/infrastructure/config/types";
import { DiscordClient } from "@/infrastructure/discord/client";
import { ClientReadyEvent } from "@/infrastructure/discord/events/client-ready-event";
import type { IDiscordGuildEvent } from "@/infrastructure/discord/events/i-events";
import { SlashCommandEvent } from "@/infrastructure/discord/events/slash-command-event";
import { RedisRepository } from "@/infrastructure/cache/redis-repository";
import { JikanService } from "@/infrastructure/jikan/jikan-service";
import { RedisService } from "@/infrastructure/cache/redis-service";
import { AboutCommand } from "@/interfaces/discord/commands/general/about-command";
import { CmdsCommand } from "@/interfaces/discord/commands/general/cmds-command";
import { PingCommand } from "@/interfaces/discord/commands/general/ping-command";
import { AnimeCommand } from "@/interfaces/discord/commands/searches/anime-command";
import { MalCommand } from "@/interfaces/discord/commands/searches/mal-command";
import type { IDiscordGuildCommand } from "@/interfaces/discord/types/i-command";
import type { ChatInputCommandInteraction } from "discord.js";
import { Container } from "inversify";
import { createLogger, format, type Logger, transports } from "winston";

const container: Container = new Container();

/*
 * Core
 */
container.bind<Logger>(Types.Logger).toConstantValue(
  createLogger({
    levels: {
      fatal: 0,
      error: 1,
      warn: 2,
      info: 3,
      debug: 4,
      trace: 5
    },
    level: process.env.LOG_LEVEL || "info",
    format: format.combine(format.timestamp(), format.errors({ stack: true }), format.json()),
    transports: [
      new transports.Console({ level: "info" }),
      new transports.File({ filename: "logs/error.log", level: "error" }),
      new transports.File({ filename: "logs/combined.log" })
    ]
  })
);

/*
 * Application
 */
container
  .bind<AbstractBaseUseCase<ChatInputCommandInteraction, void>>(AboutCommandUseCase)
  .to(AboutCommandUseCase);
container
  .bind<AbstractBaseUseCase<ChatInputCommandInteraction, void>>(PingCommandUseCase)
  .to(PingCommandUseCase);
container
  .bind<AbstractBaseUseCase<ChatInputCommandInteraction, void>>(CmdsCommandUseCase)
  .to(CmdsCommandUseCase);

container
  .bind<AbstractBaseUseCase<ChatInputCommandInteraction, void>>(AnimeCommandUseCase)
  .to(AnimeCommandUseCase);
container
  .bind<AbstractBaseUseCase<ChatInputCommandInteraction, void>>(MalCommandUseCase)
  .to(MalCommandUseCase);

/*
 * Infrastructure
 */
container.bind(Settings).toSelf().inSingletonScope();

container.bind(RedisService).toSelf();
container.bind(RedisRepository).toSelf().inSingletonScope();
container.bind(JikanService).toSelf();

container.bind(DiscordClient).toSelf().inSingletonScope();
container.bind<IDiscordGuildEvent>(Types.DiscordGuildEvent).to(ClientReadyEvent);
container.bind<IDiscordGuildEvent>(Types.DiscordGuildEvent).to(SlashCommandEvent);

/*
 * Interfaces
 */
container.bind<IDiscordGuildCommand>(Types.DiscordGuildCommand).to(PingCommand);
container.bind<IDiscordGuildCommand>(Types.DiscordGuildCommand).to(AboutCommand);
container.bind<IDiscordGuildCommand>(Types.DiscordGuildCommand).to(CmdsCommand);

container.bind<IDiscordGuildCommand>(Types.DiscordGuildCommand).to(AnimeCommand);
container.bind<IDiscordGuildCommand>(Types.DiscordGuildCommand).to(MalCommand);

export { container, container as iocContainer };
