# Aura

Aura is a multi-feature Discord bot built with Node.js, TypeScript, Discord.js, Inversify, and i18next.

## Commands

| Command                | Description                                                                                                                |
| ---------------------- | -------------------------------------------------------------------------------------------------------------------------- |
| `/about`               | Shows bot information, author, version, and uptime placeholder.                                                            |
| `/cmds`                | Lists available commands grouped by category.                                                                              |
| `/ping`                | Replies with the current Discord websocket latency.                                                                        |
| `/anime title:<title>` | Searches for an anime on MyAnimeList and returns an embed with synopsis, score, rank, genres, studios, trailer, and image. |
| `/mal name:<username>` | Searches for a MyAnimeList user and returns an embed with anime statistics and profile details.                            |

## Requirements

- Node.js 22 is recommended. The Docker image uses `node:22`, and this project was checked locally with Node.js `v22.17.1`.
- npm `10.9.2` or newer.
- A Discord application and bot token.
- Redis, either installed locally or started through Docker Compose.

## Environment Variables

Create a `.env` file from `.env.example` and fill in the values for your bot:

```env
### Discord Bot
BOT_ID=
BOT_TOKEN=
BOT_OWNERID=
BOT_PRESENCE_TYPE=
BOT_PRESENCE_MSG=
BOT_DEFAULT_LANGUAGE=en

### Logging
LOG_LEVEL=info
LOGS_PATH=/path/to/logs

### Redis
# REDIS_HOST=localhost
REDIS_TTL=3600
```

| Variable               | Required    | Default     | Description                                                                                                       |
| ---------------------- | ----------- | ----------- | ----------------------------------------------------------------------------------------------------------------- |
| `BOT_ID`               | Yes         | -           | Discord application client ID used to register slash commands.                                                    |
| `BOT_TOKEN`            | Yes         | -           | Discord bot token used to log in and register commands.                                                           |
| `BOT_PRESENCE_MSG`     | No          | `Discord`   | Activity text shown in the bot presence.                                                                          |
| `BOT_PRESENCE_TYPE`    | No          | `0`         | Discord activity type. `0` Playing, `1` Streaming, `2` Listening, `3` Watching, `4` Custom Status, `5` Competing. |
| `BOT_DEFAULT_LANGUAGE` | No          | `en`        | Locale used for command descriptions and bot messages. Available files live in `src/locales`.                     |
| `LOG_LEVEL`            | No          | `info`      | Winston log level.                                                                                                |
| `LOGS_PATH`            | Docker only | -           | Host directory mounted to `/app/logs` by Docker Compose.                                                          |
| `REDIS_HOST`           | No          | `localhost` | Redis host used by the cache repository. Docker Compose sets this to `bot-cache`.                                 |
| `REDIS_TTL`            | No          | `3600`      | Cache duration in seconds.                                                                                        |
| `BOT_OWNERID`          | No          | -           | Present in Docker Compose and `.env.example`; not currently used by the application code.                         |

## Installation

```bash
npm install
```

## Running Locally

Start Redis first, then run the bot in development mode:

```bash
npm run dev
```

The development command loads `.env`, runs `src/index.ts` with `ts-node-dev`, and registers the slash commands before logging in.

## Localization

Aura uses `i18next` through `TranslationService`. Locale files are stored in
`src/locales`.

Available locales:

- `en`: English
- `pt`: Brazilian Portuguese

Set `BOT_DEFAULT_LANGUAGE` in `.env` to choose the active locale:

```env
BOT_DEFAULT_LANGUAGE=pt
```

When building the project, `npm run postbuild` copies `src/locales` into
`artifacts/dist/locales` so the compiled bot can load translations at runtime.

## Running With Docker

```bash
docker compose -p aura-bot up --build -d
```

Docker Compose starts two services:

- `bot-cache`: a Redis container exposed on port `6379`.
- `bot`: the Aura bot container, configured through the environment variables from `.env`.

## Scripts

| Script              | Description                                                                                 |
| ------------------- | ------------------------------------------------------------------------------------------- |
| `npm run dev`       | Starts the bot in development mode with `ts-node-dev` and `tsconfig-paths`.                 |
| `npm run lint`      | Runs ESLint for TypeScript files.                                                           |
| `npm run build`     | Runs the TypeScript compiler, rewrites path aliases, and copies locales.                    |
| `npm run prebuild`  | Runs automatically before `npm run build`; currently executes linting.                      |
| `npm run postbuild` | Runs automatically after `npm run build`; copies `src/locales` to `artifacts/dist/locales`. |
