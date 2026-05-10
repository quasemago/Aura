import * as Types from "@/infrastructure/config/types";
import { ClientReadyEvent } from "@/infrastructure/discord/events/client-ready-event";
import { SlashCommandEvent } from "@/infrastructure/discord/events/slash-command-event";
import { AboutCommand } from "@/interfaces/discord/commands/general/about-command";
import { CmdsCommand } from "@/interfaces/discord/commands/general/cmds-command";
import { PingCommand } from "@/interfaces/discord/commands/general/ping-command";
import { AnimeCommand } from "@/interfaces/discord/commands/searches/anime-command";
import { MalCommand } from "@/interfaces/discord/commands/searches/mal-command";
import { Container } from "typedi";
import { createLogger, format, type Logger, transports } from "winston";

const container = Container;

/*
 * Core
 */
container.set<Logger>(
  Types.Logger,
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

container.set(Types.DiscordGuildEvent, [
  container.get(ClientReadyEvent),
  container.get(SlashCommandEvent)
]);

container.set(Types.DiscordGuildCommand, [
  container.get(PingCommand),
  container.get(AboutCommand),
  container.get(CmdsCommand),
  container.get(AnimeCommand),
  container.get(MalCommand)
]);

export { container, container as iocContainer };
