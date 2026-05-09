import { Settings } from "@/infrastructure/config/settings";
import * as Types from "@/infrastructure/config/types";
import { IDiscordGuildEvent } from "@/infrastructure/discord/events/i-events";
import { IDiscordGuildCommand } from "@/interfaces/discord/types/i-command";
import { Client, GatewayIntentBits, Partials, REST, Routes } from "discord.js";
import { inject, injectable, multiInject } from "inversify";
import { Logger } from "winston";

@injectable()
export class DiscordClient extends Client {
  constructor(
    @inject(Types.Logger) private readonly logger: Logger,
    @inject(Settings) private readonly settings: Settings,
    @multiInject(Types.DiscordGuildEvent) private readonly events: IDiscordGuildEvent[],
    @multiInject(Types.DiscordGuildCommand) private readonly commands: IDiscordGuildCommand[]
  ) {
    super({
      intents: [
        GatewayIntentBits.Guilds,
        GatewayIntentBits.GuildMessages,
        GatewayIntentBits.MessageContent,
        GatewayIntentBits.GuildMembers,
        GatewayIntentBits.DirectMessages,
        GatewayIntentBits.DirectMessageReactions,
        GatewayIntentBits.GuildMessageReactions
      ],
      partials: [Partials.Message, Partials.Channel]
    });
  }

  public async login(token: string): Promise<string> {
    return super.login(token);
  }

  public async start() {
    this.logger.info("Starting Discord client...");
    const discordToken = this.settings.getDiscordToken();

    await this.loadEvents();
    await this.loadCommands(discordToken);

    await this.login(discordToken);
    this.logger.info("Discord client logged in");
  }

  public getEvents(): IDiscordGuildEvent[] {
    return this.events;
  }

  public getCommands(): IDiscordGuildCommand[] {
    return this.commands;
  }

  private async loadEvents(): Promise<void> {
    this.logger.info(`Loading ${this.events.length} events...`);
    for (const event of this.events) {
      this.logger.info(`Registering event: ${event.constructor.name}`);
      event.handle(this);
    }
    this.logger.info("All events loaded successfully.");
  }

  private async loadCommands(token: string): Promise<void> {
    this.logger.info(`Loading ${this.commands.length} commands...`);
    const commandsList = [];

    for (const command of this.commands) {
      commandsList.push(command.data.toJSON());
    }

    const commandsData = (await new REST()
      .setToken(token)
      .put(Routes.applicationCommands(this.settings.getDiscordClientId()), {
        body: commandsList
      })) as [];

    this.logger.info(
      `All commands loaded successfully. Refreshed ${commandsData.length} commands.`
    );
  }
}
