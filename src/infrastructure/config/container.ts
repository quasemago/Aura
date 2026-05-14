import { AboutCommandUseCase } from "@/application/usecases/general/about-command-usecase";
import { CmdsCommandUseCase } from "@/application/usecases/general/cmds-command-usecase";
import { PingCommandUseCase } from "@/application/usecases/general/ping-command-usecase";
import { AnimeCommandUseCase } from "@/application/usecases/searches/anime-command-usecase";
import { MalCommandUseCase } from "@/application/usecases/searches/mal-command-usecase";
import { RedisRepository } from "@/infrastructure/cache/redis-repository";
import { RedisService } from "@/infrastructure/cache/redis-service";
import { Settings } from "@/infrastructure/config/settings";
import * as Types from "@/infrastructure/config/types";
import { DiscordClient } from "@/infrastructure/discord/client";
import { ClientReadyEvent } from "@/infrastructure/discord/events/client-ready-event";
import { SlashCommandEvent } from "@/infrastructure/discord/events/slash-command-event";
import { TranslationService } from "@/infrastructure/i18n/translation-service";
import { JikanService } from "@/infrastructure/jikan/jikan-service";
import { AboutCommand } from "@/interfaces/discord/commands/general/about-command";
import { CmdsCommand } from "@/interfaces/discord/commands/general/cmds-command";
import { PingCommand } from "@/interfaces/discord/commands/general/ping-command";
import { AnimeCommand } from "@/interfaces/discord/commands/searches/anime-command";
import { MalCommand } from "@/interfaces/discord/commands/searches/mal-command";
import { Container } from "inversify";
import { createLogger, format, type Logger, transports } from "winston";

const container = new Container();

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

container.bind(Settings).toSelf().inSingletonScope();
container.bind(RedisRepository).toSelf().inSingletonScope();
container.bind(RedisService).toSelf();
container.bind(JikanService).toSelf();
container.bind(TranslationService).toSelf().inSingletonScope();
container.bind(DiscordClient).toSelf().inSingletonScope();

container.bind(ClientReadyEvent).toSelf().inSingletonScope();
container.bind(SlashCommandEvent).toSelf().inSingletonScope();

container.bind(PingCommandUseCase).toSelf();
container.bind(AboutCommandUseCase).toSelf();
container.bind(CmdsCommandUseCase).toSelf();
container.bind(AnimeCommandUseCase).toSelf();
container.bind(MalCommandUseCase).toSelf();

container.bind(PingCommand).toSelf();
container.bind(AboutCommand).toSelf();
container.bind(CmdsCommand).toSelf();
container.bind(AnimeCommand).toSelf();
container.bind(MalCommand).toSelf();

container
  .bind(Types.DiscordGuildEvent)
  .toDynamicValue((context) => [context.get(ClientReadyEvent), context.get(SlashCommandEvent)]);

container
  .bind(Types.DiscordGuildCommand)
  .toDynamicValue((context) => [
    context.get(PingCommand),
    context.get(AboutCommand),
    context.get(CmdsCommand),
    context.get(AnimeCommand),
    context.get(MalCommand)
  ]);

export { container, container as iocContainer };
