import type { AbstractBaseUseCase } from "@/main/application/usecases/base-usecase";
import { AboutCommandUseCase } from "@/main/application/usecases/general/about-command-usecase";
import { CmdsCommandUseCase } from "@/main/application/usecases/general/cmds-command-usecase";
import { PingCommandUseCase } from "@/main/application/usecases/general/ping-command-usecase";
import { AnimeCommandUseCase } from "@/main/application/usecases/searches/anime-command-usecase";
import { MalCommandUseCase } from "@/main/application/usecases/searches/mal-command-usecase";
import { Settings } from "@/main/infrastructure/configurations/settings";
import * as Types from "@/main/infrastructure/configurations/types";
import { DiscordClient } from "@/main/infrastructure/discord/client";
import { ClientReadyEvent } from "@/main/infrastructure/discord/events/client-ready-event";
import type { IDiscordGuildEvent } from "@/main/infrastructure/discord/events/i-events";
import { SlashCommandEvent } from "@/main/infrastructure/discord/events/slash-command-event";
import { RedisRepository } from "@/main/infrastructure/repositories/redis/redis-repository";
import { JikanService } from "@/main/infrastructure/services/jikan/jikan-service";
import { RedisService } from "@/main/infrastructure/services/redis/redis-service";
import { AboutCommand } from "@/main/interfaces/commands/general/about-command";
import { CmdsCommand } from "@/main/interfaces/commands/general/cmds-command";
import { PingCommand } from "@/main/interfaces/commands/general/ping-command";
import { AnimeCommand } from "@/main/interfaces/commands/searches/anime-command";
import { MalCommand } from "@/main/interfaces/commands/searches/mal-command";
import type { IDiscordGuildCommand } from "@/main/interfaces/types/i-command";
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
