import { AboutCommandUseCase } from "@/main/application/usecases/about-command-usecase";
import type { AbstractBaseUseCase } from "@/main/application/usecases/base-usecase";
import { CmdsCommandUseCase } from "@/main/application/usecases/cmds-command-usecase";
import { PingCommandUseCase } from "@/main/application/usecases/ping-command-usecase";
import { Settings } from "@/main/infrastructure/configurations/settings";
import * as Types from "@/main/infrastructure/configurations/types";
import { DiscordClient } from "@/main/infrastructure/discord/client";
import { ClientReadyEvent } from "@/main/infrastructure/discord/events/client-ready-event";
import type { IDiscordGuildEvent } from "@/main/infrastructure/discord/events/i-events";
import { SlashCommandEvent } from "@/main/infrastructure/discord/events/slash-command-event";
import { RedisRepository } from "@/main/infrastructure/repositories/redis/redis-repository";
import { RedisService } from "@/main/infrastructure/services/redis/redis-service";
import { AboutCommand } from "@/main/interfaces/commands/general/about-command";
import { CmdsCommand } from "@/main/interfaces/commands/general/cmds-command";
import { PingCommand } from "@/main/interfaces/commands/general/ping-command";
import type { IDiscordGuildCommand } from "@/main/interfaces/commands/i-command";
import type { CommandInteraction } from "discord.js";
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
  .bind<AbstractBaseUseCase<CommandInteraction, void>>(AboutCommandUseCase)
  .to(AboutCommandUseCase);
container
  .bind<AbstractBaseUseCase<CommandInteraction, void>>(PingCommandUseCase)
  .to(PingCommandUseCase);
container
  .bind<AbstractBaseUseCase<CommandInteraction, void>>(CmdsCommandUseCase)
  .to(CmdsCommandUseCase);

/*
 * Infrastructure
 */
container.bind(Settings).toSelf().inSingletonScope();

container.bind(RedisService).toSelf();
container.bind(RedisRepository).toSelf().inSingletonScope();

container.bind(DiscordClient).toSelf().inSingletonScope();
container.bind<IDiscordGuildEvent>(Types.DiscordGuildEvent).to(ClientReadyEvent);
container.bind<IDiscordGuildEvent>(Types.DiscordGuildEvent).to(SlashCommandEvent);

/*
 * Interfaces
 */
container.bind<IDiscordGuildCommand>(Types.DiscordGuildCommand).to(PingCommand);
container.bind<IDiscordGuildCommand>(Types.DiscordGuildCommand).to(AboutCommand);
container.bind<IDiscordGuildCommand>(Types.DiscordGuildCommand).to(CmdsCommand);

export { container, container as iocContainer };
