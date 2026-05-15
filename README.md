# Aura

Aura is a multi-feature Discord bot built with Java, Spring Boot, Discord4J,
OpenFeign and Redis.

## Commands

| Command                | Description                                                                                                                |
| ---------------------- | -------------------------------------------------------------------------------------------------------------------------- |
| `/about` or `/ajuda`   | Shows bot information, author, version, and uptime.                                                                        |
| `/cmds`                | Lists available commands grouped by category.                                                                              |
| `/ping`                | Replies with the current Discord websocket latency.                                                                        |
| `/anime title:<title>` | Searches for an anime on MyAnimeList and returns an embed with synopsis, score, rank, genres, studios, trailer, and image. |
| `/mal name:<username>` | Searches for a MyAnimeList user and returns an embed with anime statistics and profile details.                            |

The active command names and messages come from `BOT_DEFAULT_LANGUAGE`.

## Requirements

- JDK 25.
- Maven 3.6.3 or newer.
- A Discord application and bot token.
- Redis, either installed locally or started through Docker Compose.

## Environment

Create a `.env` file from `.env.example` and fill in the Discord values:

```env
BOT_ID=
BOT_TOKEN=
BOT_DEFAULT_LANGUAGE=en
REDIS_HOST=localhost
```

| Variable               | Required    | Default                    | Description                                                                                                       |
| ---------------------- | ----------- | -------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| `BOT_ID`               | Yes         | -                          | Discord application client ID used to register slash commands.                                                    |
| `BOT_TOKEN`            | Yes         | -                          | Discord bot token used to log in and register commands.                                                           |
| `BOT_PRESENCE_MSG`     | No          | `Discord`                  | Activity text shown in the bot presence.                                                                          |
| `BOT_PRESENCE_TYPE`    | No          | `0`                        | Discord activity type. `0` Playing, `1` Streaming, `2` Listening, `3` Watching, `4` Custom Status, `5` Competing. |
| `BOT_DEFAULT_LANGUAGE` | No          | `en`                       | Locale used for command descriptions and bot messages. Available locales live in `src/main/resources/locales`.    |
| `AURA_VERSION`         | No          | `1.0.0`                    | Version shown by the about command and used in the Jikan User-Agent.                                              |
| `LOG_LEVEL`            | No          | `INFO`                     | Root log level.                                                                                                   |
| `LOGS_PATH`            | Docker only | `./logs`                   | Host directory mounted to `/app/logs` by Docker Compose.                                                          |
| `REDIS_HOST`           | No          | `localhost`                | Redis host. Docker Compose sets this to `bot-cache`.                                                              |
| `REDIS_PORT`           | No          | `6379`                     | Redis port.                                                                                                       |
| `REDIS_TTL`            | No          | `3600`                     | Cache duration in seconds.                                                                                        |
| `JIKAN_API_URL`        | No          | `https://api.jikan.moe/v4` | Base URL used by the OpenFeign Jikan client.                                                                      |

## Running Locally

Start Redis first, then run:

```bash
mvn spring-boot:run
```

## Building

```bash
mvn clean package
```

The executable jar is created in `target/aura-1.0.0.jar`.

## Running With Docker

```bash
docker compose -p aura-bot up --build -d
```

Docker Compose starts:

- `bot-cache`: Redis exposed on port `6379`.
- `bot`: the Aura bot container configured from `.env`.
